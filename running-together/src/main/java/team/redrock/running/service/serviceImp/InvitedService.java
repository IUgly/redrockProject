package team.redrock.running.service.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.InvitedDao;
import team.redrock.running.vo.InviteInfo;
import team.redrock.running.vo.User;

import java.util.HashMap;

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
    public void inviteUser(InviteInfo inviteInfo){
        this.invitedDao.insertInvited(inviteInfo);
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
    public InviteInfo receive(String student_id){
        User user = (User) this.redisTemplate.opsForHash().get(USER_REDIS, student_id);
        return user.deQueue();
    }

}
