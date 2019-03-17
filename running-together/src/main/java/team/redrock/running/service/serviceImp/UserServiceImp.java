package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import team.redrock.running.dao.UserDao;
import team.redrock.running.util.SerializeUtil;
import team.redrock.running.vo.User;
import team.redrock.running.vo.UserOtherInfo;

import java.util.List;

@Service
@Component
public class UserServiceImp {
    public static final String USER_REDIS = "User016";
    @Autowired
    private UserDao userDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Async
    public void updateUserInfoToRedis(User userInfo) {
        String student_id = userInfo.getStudent_id();
        String strUserInfo = SerializeUtil.serialize(userInfo);
        this.stringRedisTemplate.opsForValue().set(USER_REDIS+student_id, strUserInfo);
    }

    public String head_img(String student_id){
        return this.userDao.getHead_img(student_id);
    }

    @Async
    public void updateHeadImg(String url, String student_id){
        this.userDao.updateHead_img(url, student_id);
    }


    public User login(String student_id, String password) {
        try {
            String url = "https://wx.idsbllp.cn/api/verify";

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("stuNum", student_id);
            map.add("idNum", password);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<JSONObject> response = restTemplate.postForEntity( url, request , JSONObject.class );

            String status = response.getBody().getString("status");
            if (!status.equals("200")){
                return null;
            }
            JSONObject json = response.getBody().getJSONObject("data");
            User user = new User(json);

            User userForData = selectUserInfo(student_id);
            if (userForData==null){
                user.setNickname("取个昵称吧");
                return user;
            }else {
                return userForData;
            }
        }catch (Exception e){
            return null;
        }

    }
    @Async
    public void insertUser(User user) {
        if (this.userDao.selectSimpleUserInfo(user.getStudent_id())==null){
            if (user.getNickname()==null){
                user.setNickname("取个昵称吧");
            }
            try {
                updateUserInfo(user);
            }catch (Exception e){
                e.printStackTrace();
            }
            this.userDao.insertUser(user);
        }
    }

    public Boolean updateUserInfo(User user) {
        updateUserInfoToRedis(user);
        return this.userDao.updateUserInfo(user);
    }

    /**
     * 循环依赖，redis中若没有相应的用户，则从mysql拉取存入redis
     * @param student_id
     * @return
     */
    public User selectUserInfo(String student_id) {
        User user = new User();
        String strUserInfo = this.stringRedisTemplate.opsForValue().get(USER_REDIS + student_id);
        if (strUserInfo != null){
            user = (User) SerializeUtil.deserialize(strUserInfo);
            return new User(user);
        }else {
            user = this.userDao.selectUserByStudentId(student_id);
            if (user != null){
                updateUserInfoToRedis(user);
                return new User(user);
            }
            return null;
        }
    }
    public List<User> selectUserListByName(String name){
        return this.userDao.getUserListByName(name);
    }

    public UserOtherInfo selectUserOtherInfo(String student_id){
        return this.userDao.getUserOtherInfo(student_id);
    }
}
