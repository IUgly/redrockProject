package team.redrock.running.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import team.redrock.running.bean.ResponseBean;
import team.redrock.running.dto.InvitationSend;
import team.redrock.running.enums.UnicomResponseEnums;
import team.redrock.running.service.serviceImp.InvitedService;
import team.redrock.running.service.serviceImp.UpdateScoreService;
import team.redrock.running.service.serviceImp.UserServiceImp;
import team.redrock.running.util.Decrypt;
import team.redrock.running.util.Util;
import team.redrock.running.vo.InviteInfo;
import team.redrock.running.vo.Record;
import team.redrock.running.vo.User;

import java.util.HashMap;
import java.util.List;

@RestController
public class InvitedControl {
    public static final String USER_REDIS = "User016";
    public static final String INVITATION_REDIS = "InvitationRedis016";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private InvitedService invitedService;
    @Autowired
    private UpdateScoreService updateScoreService;

    @PostMapping(value = "/invite/update", produces = "application/json")
    public String Upload(String student_id, String invitees){
        User invite_user = this.userServiceImp.selectUserInfo(student_id);
        InviteInfo inviteInfo = new InviteInfo(invite_user, invitees);
        this.invitedService.startInvited(inviteInfo);
        //异步处理
        this.invitedService.sendInvitations(invitees, inviteInfo);
        this.invitedService.updatesUserInvitationInfo(inviteInfo, invite_user);
        return JSONObject.toJSONString(new ResponseBean<>(inviteInfo.getInvited_id(), UnicomResponseEnums.SUCCESS));
    }

    //得到最近一次邀约结果
    @GetMapping(value = "/invite/history/result", produces = "application/json")
    public String getInvitedResult(String student_id){
        JSONArray jsonArray = this.invitedService.getInvitedResult(student_id);
        if (jsonArray == null){
            return JSONObject.toJSONString(new ResponseBean(UnicomResponseEnums.NO_SEND_INVITATION));
        }
        return JSONObject.toJSONString(new ResponseBean<>(jsonArray, UnicomResponseEnums.SUCCESS));
    }
    @PostMapping(value = "invite/searchinfo", produces = "application/json")
    public String searchInfo(String info){
        if (!Util.isLetterDigitOrChinese(info)){
            List<User> userList = this.userServiceImp.selectUserListByName(info);
            JSONArray jsonArray = new JSONArray();
            userList.stream().forEach( user -> jsonArray.add(user));
            return JSONObject.toJSONString(new ResponseBean<>(jsonArray, UnicomResponseEnums.SUCCESS));
        }else {
            User user = this.userServiceImp.selectUserInfo(info);
            if (user==null){
                return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.NOT_STUDENT_OR_NOT_REGISTER));
            }else {
                return JSONObject.toJSONString(new ResponseBean<>(user, UnicomResponseEnums.SUCCESS));
            }
        }
    }
    @GetMapping(value = "/invite/invited", produces = "application/json")
    public String getInvitedOrNot(String student_id) {
        User user = this.userServiceImp.selectUserInfo(student_id);
        InvitationSend invitationReceive = user.deQueueInvitation();
        this.userServiceImp.updateUserInfoToRedis(user);
        if (invitationReceive != null) {
            return JSONObject.toJSONString(new ResponseBean<>(invitationReceive, UnicomResponseEnums.SUCCESS));
        } else {
            return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.NOT_INVITED_INFO));
        }
    }
    @PostMapping(value = "/invite/result", produces = "application/json")
    public String receiveOrOther(String invited_id, String student_id, String result){
        InviteInfo inviteInfo = this.invitedService.getInvitedById(invited_id);
        inviteInfo.getResult().put(student_id, result);
        this.invitedService.updateInvitationInfo(inviteInfo);

        User user = this.userServiceImp.selectUserInfo(student_id);
        user.setInvitingNow(invited_id);
        this.userServiceImp.updateUserInfoToRedis(user);
        return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.SUCCESS));
    }
    @PostMapping(value = "/invite/update_data", produces = "application/json")
    public String inviteUpdateRunData(String student_id, String invited_id, String run_data){
        String info = null;
        try {
            info = Decrypt.aesDecryptString(run_data);
        }catch (Exception e){
            return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.RUNDATA_ERROR));
        }
        JSONObject json = JSONObject.parseObject(info);
        InviteInfo inviteInfo = this.invitedService.getInvitedById(invited_id);

        Record record = new Record(json, invited_id, student_id);
        this.updateScoreService.notInvitedUpdate(record);

        Double score = Double.parseDouble(json.getString("distance"));
        this.invitedService.invitationToMysql(student_id, score/100 * inviteInfo.getAcceptUsersNum());
        return JSONObject.toJSONString(new ResponseBean<>(new Record(json, invited_id), UnicomResponseEnums.SUCCESS));
    }

    //轮询邀约是否结束
    @GetMapping(value = "/invite/end", produces = "application/json")
    public String invitedEnd(String invited_id){
        InviteInfo inviteInfo = this.invitedService.getInvitedById(invited_id);
        HashMap<String, String> resultMap = new HashMap<>();
        if (inviteInfo!=null){
            resultMap.put("result", "OK");
            return JSONObject.toJSONString(new ResponseBean<>(new InviteInfo("OK"), UnicomResponseEnums.SUCCESS));
        }
        resultMap.put("result", "END");
        return JSONObject.toJSONString(new ResponseBean<>(resultMap, UnicomResponseEnums.SUCCESS));
    }
    @GetMapping (value = "/invite/history", produces = "application/json")
    public String invitedHistory(String student_id, Integer page){
        if (page == null){
            page = 1;
        }
        String result = this.stringRedisTemplate.opsForValue().get(INVITATION_REDIS+student_id+page);
        if (result == null){
            result = this.invitedService.getInvitedHistory(student_id, page);
            this.stringRedisTemplate.opsForValue().set(INVITATION_REDIS+student_id+page, result);
        }
        return result;
    }

    @PostMapping(value = "/invite/cancel", produces = "application/json")
    public String cancelInvitation(String invited_id){
        this.invitedService.cancelInvited(invited_id);
        return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.SUCCESS));
    }
}
