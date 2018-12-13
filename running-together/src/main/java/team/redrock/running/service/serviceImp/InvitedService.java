package team.redrock.running.service.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.vo.User;

@Service
@Component
public class InvitedService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserServiceImp userServiceImp;
    public String selectUserState(String student_id){
        User user = this.userServiceImp.selectUserInfo(student_id);
        String state = user.getState();
        return state;
    }
    public String inviteUser(String[] student_id){
        for (int i=0; i<student_id.length; i++){
            User user = this.userServiceImp.selectUserInfo(student_id[i]);
            user.setState("1");
            this.userServiceImp.updateUserInfo(user);
            this.redisTemplate.opsForHash().put("","","");
        }
        return "";

    }
}
