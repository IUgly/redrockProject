package team.redrock.running.controller;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import team.redrock.running.bean.ResponseBean;
import team.redrock.running.configuration.Config;
import team.redrock.running.enums.UnicomResponseEnums;
import team.redrock.running.service.serviceImp.RecordServiceImp;
import team.redrock.running.service.serviceImp.UpdateScoreService;
import team.redrock.running.service.serviceImp.UserServiceImp;
import team.redrock.running.util.AbstractBaseController;
import team.redrock.running.util.Token;
import team.redrock.running.vo.Record;
import team.redrock.running.vo.User;
import team.redrock.running.vo.UserOtherInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RestController
public class UserControl extends AbstractBaseController {
    public static final String DISTANCE_RECORD = "distanceRecord";
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private UpdateScoreService updateScoreService;
    @Autowired
    private Config config;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RecordServiceImp recordServiceImp;
    @PostMapping(value = "/user/login", produces = "application/json")
    public String login(String student_id, String password){
        //验证帐号密码是否正确
        User user = this.userServiceImp.login(student_id, password);
        if (user != null){
            User responseUser = this.userServiceImp.selectUserInfo(student_id);
            if (responseUser ==null){
                this.userServiceImp.insertUser(user);
            }
            //token有效时间 30 min
            Token token = new Token(user.getName(), new Date());
            responseUser.setToken(token.CreateToken());
            this.userServiceImp.insertUserToRedis(student_id, responseUser);
            return JSONObject.toJSONString(new ResponseBean<>(responseUser, UnicomResponseEnums.SUCCESS));
        }else {
            return JSONObject.toJSONString(new ResponseBean<>(
                    UnicomResponseEnums.INVALID_PASSWORD));
        }
    }
    @GetMapping(value = "/user/loginout", produces = "application/json")
    public String loginOut(String student_id){
        return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.SUCCESS));
    }

    @PostMapping(value = "user/info/update", produces = "application/json")
    public String updateUserInfo(String student_id, String data){
        JSONObject json = JSONObject.parseObject(data);
        User user = this.userServiceImp.selectUserInfo(student_id);
        user.setNickname(json.getString("nickname"));
        user.setStudent_id(student_id);
        this.userServiceImp.updateUserInfo(user);

        return JSONObject.toJSONString(new ResponseBean<>(
                this.userServiceImp.selectUserInfo(student_id),
                UnicomResponseEnums.SUCCESS));
    }
    @PostMapping(value = "/user/distance/update", produces = "application/json")
    public String update(@RequestBody JSONObject json){
        //插入跑步数据到mysql
        Record record = new Record(json);
//        this.iRecordService.addRecord("distance", record);
        this.updateScoreService.notInvitedUpdate(record);
        return JSONObject.toJSONString(new ResponseBean<>(record,UnicomResponseEnums.SUCCESS));
    }
    @GetMapping(value = "/user/lat_lng", produces = "application/json")
    public String getLatLngList(String student_id,Integer page){
        if (page == null){
            page = 1;
        }
        String result = (String) this.redisTemplate.opsForHash().get(DISTANCE_RECORD, student_id + page);
        if (result == null){
            result =this.recordServiceImp.getLatLngList(student_id, page);
        }
        return result;
    }
    @GetMapping(value = "/user/history/detail", produces = "application/json")
    public String getHistoryDetail(String id){
        Record record = this.recordServiceImp.getRecordById(id);
        return JSONObject.toJSONString(new ResponseBean<>(record, UnicomResponseEnums.SUCCESS));
    }
    @GetMapping(value = "/user/info", produces = "application/json")
    public String getUserOtherInfo(String student_id){
        UserOtherInfo userOtherInfo = this.userServiceImp.selectUserOtherInfo(student_id);
        return JSONObject.toJSONString(new ResponseBean<>(userOtherInfo, UnicomResponseEnums.SUCCESS));
    }
    @ResponseBody
    @PostMapping(value = "/user/upload", produces = "application/json")
    public String upload(HttpServletRequest request) {
        String student_id = request.getParameter("student_id");
        if (request instanceof MultipartHttpServletRequest) { // 如果你现在是MultipartHttpServletRequest对象
            MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = mrequest.getFiles("file");
            Iterator<MultipartFile> iter = files.iterator();
            while (iter.hasNext()) {
                MultipartFile file = iter.next() ;
                if (file != null) { // 现在有文件上传
                    try {
                        byte[] bytes = file.getBytes();
                        long fileName = System.currentTimeMillis();
                        Path path = Paths.get(this.config.getPhoto() +"/"+ student_id+".jpg");
//                        String url = this.config.getPhotoUrl()+"/"+student_id+".jpg";
                        Files.write(path, bytes);
                        return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.SUCCESS));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.UPLOAD_FAIL));
    }

}
