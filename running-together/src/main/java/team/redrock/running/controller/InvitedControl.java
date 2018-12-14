package team.redrock.running.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import team.redrock.running.bean.ResponseBean;
import team.redrock.running.enums.UnicomResponseEnums;
import team.redrock.running.service.serviceImp.InvitedService;
import team.redrock.running.service.serviceImp.RecordServiceImp;
import team.redrock.running.service.serviceImp.UserServiceImp;
import team.redrock.running.util.Util;
import team.redrock.running.vo.InviteInfo;
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
    @PostMapping(value = "invite/update", produces = "application/json")
    public String Upload(String student_id, String invitees){
        User invite_user = (User) this.redisTemplate.opsForHash().get(USER_REDIS, student_id);
        InviteInfo inviteInfo = new InviteInfo(invite_user, invitees);
        this.invitedService.inviteUser(inviteInfo);
        this.invitedService.sendInvitations(invitees, invite_user);
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
    public String getInvitedOrNot(String student_id){
        User user = (User) this.redisTemplate.opsForHash().get(USER_REDIS, student_id);
        InviteInfo inviteInfo = user.deQueue();
        if (inviteInfo!=null){
            return JSONObject.toJSONString(new ResponseBean<>(inviteInfo, UnicomResponseEnums.SUCCESS));
        }else {
            return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.NOT_INVITED_INFO));
        }
    }
    @GetMapping(value = "/invite/end", produces = "application/json")
    public String getInvitationOverOrNot(String invited_id){
        return "";
    }
    @PostMapping(value = "/invite/result", produces = "application/json")
    public String receiveOrOther(String invited_id, String student_id, String result){
        InviteInfo inviteInfo = (InviteInfo) this.redisTemplate.opsForHash().get(INVITATION_REDIS, invited_id);
        switch (result){
            case "1":
                inviteInfo.getPassive_studentSet().add(student_id);
            default:
                inviteInfo.getPassive_studentSet().remove(student_id);
        }
        this.recordServiceImp.putRedisHash(invited_id, inviteInfo, INVITATION_REDIS);
        return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.SUCCESS));
    }
}
