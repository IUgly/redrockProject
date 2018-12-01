package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team.redrock.running.dao.UserDao;
import team.redrock.running.vo.User;
import team.redrock.running.vo.UserOtherInfo;

import java.util.HashMap;
import java.util.Map;

@Service
@Component
public class UserServiceImp {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Async
    public void insertUserToRedis(String student_id, User userInfo) {
        Gson gson = new Gson();
        this.redisTemplate.opsForValue().set(student_id, gson.toJson(userInfo));
    }
    public User login(String student_id, String password) {

        if (this.redisTemplate.opsForValue().get(student_id)!=null){
            return this.selectUserInfo(student_id);
        }
        RestTemplate restT = new RestTemplate();
        Map<String, String> map = new HashMap<>();
        map.put("stuNum",student_id);
        map.put("idNum", password);

        JSONObject responseEntity = restT.postForObject("http://hongyan.cqupt.edu.cn/api/verify", map, JSONObject.class);
        JSONObject json = responseEntity.getJSONObject("data");
        User user = new User(json);
        return user;
    }
    @Async
    public Boolean insertUser(User user) {
        return this.userDao.insertUser(user);
    }
    @Async
    public Boolean updateUserInfo(User user) {
        Gson gson = new Gson();
        String userInfo = gson.toJson(user);
        //用户信息插入redis（stu_id － userInfo）
        this.redisTemplate.opsForValue().getOperations().delete(user.getStudent_id());
        this.redisTemplate.opsForValue().set(user.getStudent_id(), userInfo);

        return this.userDao.updateUserInfo(user);
    }

    public User selectUserInfo(String student_id) {
        String userInfo = this.redisTemplate.opsForValue().get(student_id);
//        System.out.println("redis中:"+userInfo);
        Gson gson = new Gson();
        User user = gson.fromJson(userInfo, User.class);
        if (user!=null){
//            System.out.println("转bean："+user.toString());
            return user;
        }
        return this.userDao.selectUserByStudentId(student_id);
    }

    public User selectUserSimpleInfo(String student_id) {
        return this.userDao.selectSimpleUserInfo(student_id);
    }
    public UserOtherInfo selectUserOtherInfo(String student_id){
        return this.userDao.getUserOtherInfo(student_id);
    }
}
