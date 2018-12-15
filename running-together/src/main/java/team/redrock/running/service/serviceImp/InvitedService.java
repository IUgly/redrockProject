package team.redrock.running.service.serviceImp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.InvitedDao;
import team.redrock.running.vo.InviteInfo;
import team.redrock.running.vo.User;

import java.util.HashMap;
import java.util.List;

@Service
@Component
public class InvitedService {
    @Autowired
    private RedisTemplate redisTemplate;
    public static final String USER_REDIS = "UserRedis";
    public static final String INVITATION_REDIS = "InvitationRedis";
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private InvitedDao invitedDao;
    public String selectUserState(String student_id){
        User user = this.userServiceImp.selectUserInfo(student_id);
        String state = user.getState();
        return state;
    }
    @Async
    public void insertInvitationToRedis(InviteInfo inviteInfo){
        HashMap invitationHash = new HashMap();
        invitationHash.put(inviteInfo.getInvited_id(), inviteInfo);
        this.redisTemplate.opsForHash().putAll(INVITATION_REDIS, invitationHash);
    }
    @Async
    public void sendInvitations(String invitees, User invite_user){
        String[] student_ids = invitees.split(",");
        for (String ids: student_ids){
            InviteInfo inviteInfo = new InviteInfo(invite_user, ids);
            User user = (User) this.redisTemplate.opsForHash().get(USER_REDIS, ids);
            user.enQueue(inviteInfo);
        }
    }
    @Async
    public void OverInvitation(String invited_id, InviteInfo inviteInfo){
        this.redisTemplate.opsForHash().delete(INVITATION_REDIS, invited_id);
        inviteInfo.setResult("END");
        this.invitedDao.insertInvited(inviteInfo);
    }
    public JsonArray getInvitedHistory(String student_id){
        List<InviteInfo> inviteInfoList = this.invitedDao.selectInvitedList(student_id);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(gson.toJson(inviteInfoList), JsonArray.class);
        return jsonArray;
    }

}
