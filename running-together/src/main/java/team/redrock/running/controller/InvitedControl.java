package team.redrock.running.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.redrock.running.bean.ResponseBean;
import team.redrock.running.dto.InvitationSend;
import team.redrock.running.enums.UnicomResponseEnums;
import team.redrock.running.service.serviceImp.InvitedService;
import team.redrock.running.service.serviceImp.RecordServiceImp;
import team.redrock.running.service.serviceImp.UpdateScoreService;
import team.redrock.running.service.serviceImp.UserServiceImp;
import team.redrock.running.util.Util;
import team.redrock.running.vo.InviteInfo;
import team.redrock.running.vo.Record;
import team.redrock.running.vo.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class InvitedControl {
    public static final String USER_REDIS = "User009";
    public static final String INVITATION_REDIS = "InvitationRedis010";
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private RecordServiceImp recordServiceImp;
    @Autowired
    private InvitedService invitedService;
    @Autowired
    private UpdateScoreService updateScoreService;

    @PostMapping(value = "/invite/update", produces = "application/json")
    public String Upload(String student_id, String invitees){
        User invite_user= this.userServiceImp.selectUserInfo(student_id);
        InviteInfo inviteInfo = new InviteInfo(invite_user, invitees);
        this.invitedService.startInvited(inviteInfo);
        //异步处理
        this.invitedService.sendInvitations(invitees, inviteInfo);
        this.redisTemplate.opsForHash().put(
                INVITATION_REDIS,
                inviteInfo.getInvited_id(),
                inviteInfo);
        invite_user.setInvitingNow(inviteInfo.getInvited_id());
        this.redisTemplate.opsForHash().put(USER_REDIS, invite_user.getStudent_id(), invite_user);
        return JSONObject.toJSONString(new ResponseBean<>(inviteInfo.getInvited_id(), UnicomResponseEnums.SUCCESS));
    }
    @PostMapping(value = "invite/searchinfo", produces = "application/json")
    public String searchInfo(String info){
        if (!Util.isLetterDigitOrChinese(info)){
            List<User> userList = this.userServiceImp.selectUserListByName(info);
            JSONArray jsonArray = new JSONArray();
            for (User user: userList){
                jsonArray.add(user);
            }
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
        this.userServiceImp.insertUserToRedis(user.getStudent_id(), user);
        if (invitationReceive != null) {
            return JSONObject.toJSONString(new ResponseBean<>(invitationReceive, UnicomResponseEnums.SUCCESS));
        } else {
            return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.NOT_INVITED_INFO));
        }
    }
    @PostMapping(value = "/invite/result", produces = "application/json")
    public String receiveOrOther(String invited_id, String student_id, String result){
        InviteInfo inviteInfo = (InviteInfo) this.redisTemplate.opsForHash().get(INVITATION_REDIS, invited_id);
        inviteInfo.getResult().put(student_id, result);
        Map<String, InviteInfo> hashMap = new HashMap<>();
        hashMap.put(invited_id, inviteInfo);
        this.redisTemplate.opsForHash().putAll(INVITATION_REDIS, hashMap);
        return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.SUCCESS));
    }
    @PostMapping(value = "/invite/update_data", produces = "application/json")
    public String inviteUpdateRunData(String invited_id, @RequestBody JSONObject json){
        InviteInfo inviteInfo = (InviteInfo) this.redisTemplate.opsForHash().get(INVITATION_REDIS, invited_id);
        inviteInfo.setDistance(Double.parseDouble(json.getString("distance")));
        Map<String, String> resultMap = inviteInfo.getResult();
        resultMap.put(inviteInfo.getInvited_studentId(), "1");
        for(String key:resultMap.keySet()){
            if (resultMap.get(key).equals("1")){
                Record record = new Record(json, invited_id, key);
                this.updateScoreService.notInvitedUpdate(record);
            }
        }
        this.invitedService.OverInvitation(invited_id, inviteInfo);
        return JSONObject.toJSONString(new ResponseBean<>(new Record(json, invited_id), UnicomResponseEnums.SUCCESS));
    }
    //轮询邀约是否结束
    @GetMapping(value = "/invite/end", produces = "application/json")
    public String invitedEnd(String invited_id){
        InviteInfo inviteInfo = (InviteInfo) this.redisTemplate.opsForHash().get(INVITATION_REDIS, invited_id);
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
        String result = (String) this.redisTemplate.opsForHash().get(INVITATION_REDIS, student_id+page);
        if (result == null){
            result = this.invitedService.getInvitedHistory(student_id, page);
            this.redisTemplate.opsForHash().put(INVITATION_REDIS, student_id+page, result);
        }
        return result;
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
    @PostMapping(value = "/invite/cancel", produces = "application/json")
    public String cancelInvitation(String invited_id){
        this.invitedService.cancelInvited(invited_id);
        return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.SUCCESS));
    }
}
