package team.redrock.running.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
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

import java.util.List;

@RestController
public class InvitedControl {
    public static final String USER_REDIS = "UserRedis";
    public static final String INVITATION_REDIS = "InvitationRedis";
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
    @PostMapping(value = "invite/update", produces = "application/json")
    public String Upload(String student_id, String invitees){
        User invite_user= this.userServiceImp.selectUserInfo(student_id);
        InviteInfo inviteInfo = new InviteInfo(invite_user, invitees);
        this.invitedService.startInvited(inviteInfo);
        this.invitedService.sendInvitations(invitees, inviteInfo);
        this.invitedService.insertInvitationToRedis(inviteInfo);
        return JSONObject.toJSONString(new ResponseBean<>(inviteInfo.getInvited_id(), UnicomResponseEnums.SUCCESS));
    }
    @PostMapping(value = "invite/searchinfo", produces = "application/json")
    public String searchInfo(String info){
        if (Util.isLetterDigitOrChinese(info)){
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
        this.invitedService.needInvitedUserResult(inviteInfo, student_id, result);
        this.recordServiceImp.putRedisHash(invited_id, inviteInfo, INVITATION_REDIS);
        return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.SUCCESS));
    }
    @PostMapping(value = "/invite/update_data", produces = "application/json")
    public String inviteUpdateRunData(String invited_id, @RequestBody JSONObject json){
        InviteInfo inviteInfo = (InviteInfo) this.redisTemplate.opsForHash().get(INVITATION_REDIS, invited_id);
        Record record = new Record(json);
        record.setInvited_id(invited_id);
        String[] invited_members = inviteInfo.getPassive_studentSet();
        invited_members[invited_members.length+1] = inviteInfo.getInvited_studentId();
        for (int i=1; i< invited_members.length; i++){
            record.setStudent_id(invited_members[i]);
            this.updateScoreService.notInvitedUpdate(record);
            //更新redis的个人和班级RSET集合（日周月总榜)
            this.updateScoreService.insertOnceRunDataToRedis(record);
        }
        this.invitedService.insertInvitationToRedis(inviteInfo);
        this.invitedService.OverInvitation(invited_id, inviteInfo);
        return JSONObject.toJSONString(new ResponseBean<>(record, UnicomResponseEnums.SUCCESS));
    }
    @GetMapping(value = "/invite/end", produces = "application/json")
    public String invitedEnd(String invited_id){
        InviteInfo inviteInfo = (InviteInfo) this.redisTemplate.opsForHash().get(INVITATION_REDIS, invited_id);
        if (inviteInfo!=null){
            return JSONObject.toJSONString(new ResponseBean<>(new InviteInfo("OK"), UnicomResponseEnums.SUCCESS));
        }
        return JSONObject.toJSONString(new ResponseBean<>(new InviteInfo("END"), UnicomResponseEnums.SUCCESS));
    }
    @GetMapping (value = "/invite/history", produces = "application/json")
    public String invitedHistory(String student_id){
        JsonArray invitedHistoryJsonArray = this.invitedService.getInvitedHistory(student_id);
        return JSONObject.toJSONString(new ResponseBean<>(invitedHistoryJsonArray, UnicomResponseEnums.SUCCESS));
    }
    @GetMapping(value = "/invite/history/result", produces = "application/json")
    public String getInvitedResult(String student_id){
        JSONArray jsonArray = this.invitedService.getInvitedResult(student_id);
        return JSONObject.toJSONString(new ResponseBean<>(jsonArray, UnicomResponseEnums.SUCCESS));
    }
    @PostMapping(value = "/invite/cancel", produces = "application/json")
    public String cancelInvitation(String invited_id){
        this.invitedService.cancelInvited(invited_id);
        return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.SUCCESS));
    }

}
