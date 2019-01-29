package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSONObject;
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
import java.util.List;
import java.util.Map;

@Service
@Component
public class UserServiceImp {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisTemplate redisTemplate;
    public static final String USER_REDIS = "UserRedis";
    @Async
    public void insertUserToRedis(String student_id, User userInfo) {
        HashMap userHash = new HashMap();
        userHash.put(student_id, userInfo);
        this.redisTemplate.opsForHash().putAll(USER_REDIS, userHash);
    }
    public User login(String student_id, String password) {
        RestTemplate restT = new RestTemplate();
        Map<String, String> map = new HashMap<>();
        map.put("stuNum",student_id);
        map.put("idNum", password);

        JSONObject responseEntity = restT.postForObject("https://wx.idsbllp.cn/api/verify", map, JSONObject.class);
        String status = responseEntity.getString("status");
        if (!status.equals("200")){
            return null;
        }
        JSONObject json = responseEntity.getJSONObject("data");
        User user = new User(json);
        user.setNickname(this.userDao.selectSimpleUserInfo(student_id).getNickname());
        return user;
    }
    @Async
    public Boolean insertUser(User user) {
        return this.userDao.insertUser(user);
    }

    public Boolean updateUserInfo(User user) {
        HashMap userHash = new HashMap();
        userHash.put(user.getStudent_id(), user);
        this.redisTemplate.opsForHash().putAll(USER_REDIS, userHash);

        return this.userDao.updateUserInfo(user);
    }

    /**
     * 循环依赖，redis中若没有相应的用户，则从mysql拉取存入redis
     * @param student_id
     * @return
     */

    public User selectUserInfo(String student_id) {
        try {
            User user = (User) this.redisTemplate.opsForHash().get(USER_REDIS, student_id);
            if (user!=null){
                return new User(user);
            }
        }catch (Exception e){
            User userFromDatabase = this.userDao.selectUserByStudentId(student_id);
            this.insertUserToRedis(userFromDatabase.getStudent_id(), userFromDatabase);
            return new User(userFromDatabase);
        }
        return null;
    }
    public List<User> selectUserListByName(String name){
        return this.userDao.getUserListByName(name);
    }

    public UserOtherInfo selectUserOtherInfo(String student_id){
        return this.userDao.getUserOtherInfo(student_id);
    }
}
