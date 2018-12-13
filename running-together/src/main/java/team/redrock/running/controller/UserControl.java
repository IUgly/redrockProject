package team.redrock.running.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import team.redrock.running.bean.RankResponseBean;
import team.redrock.running.bean.ResponseBean;
import team.redrock.running.configuration.Config;
import team.redrock.running.enums.UnicomResponseEnums;
import team.redrock.running.service.serviceImp.PositionRankServiceImp;
import team.redrock.running.service.serviceImp.RecordServiceImp;
import team.redrock.running.service.serviceImp.UpdateRunDataImp;
import team.redrock.running.service.serviceImp.UserServiceImp;
import team.redrock.running.util.AbstractBaseController;
import team.redrock.running.vo.Record;
import team.redrock.running.vo.User;
import team.redrock.running.vo.UserOtherInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

@RestController
public class UserControl extends AbstractBaseController {
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private UpdateRunDataImp updateRunDataImp;
    @Autowired
    private Config config;
    @Autowired
    private PositionRankServiceImp positionRankServiceImp;
    @Autowired
    private RecordServiceImp recordServiceImp;
    @PostMapping(value = "/login", produces = "application/json")
    public String login(String student_id, String password){
        User user = this.userServiceImp.login(student_id, password);
        if (user!=null){
            this.userServiceImp.insertUser(user);
//            Token token = new Token("2017211903", new Date());
            user.setToken("11111");
            this.userServiceImp.insertUserToRedis(student_id, user);
            return JSONObject.toJSONString(new ResponseBean<>(user, UnicomResponseEnums.SUCCESS));
        }else {
            return JSONObject.toJSONString(new ResponseBean<>(
                    UnicomResponseEnums.INVALID_PASSWORD));
        }
    }
    @GetMapping(value = "loginout", produces = "application/json")
    public String loginOut(){
        return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.SUCCESS));
    }


    @PostMapping(value = "user/info/update", produces = "application/json")
    public String updateUserInfo(String student_id, String data){
        JSONObject json = JSONObject.parseObject(data);
        User user = this.userServiceImp.selectUserInfo(student_id);
        user.setNickname(json.getString("nickname"));
        user.setStudent_id(student_id);
        this.userServiceImp.updateUserInfo(user);

        System.out.println(this.userServiceImp.selectUserInfo(student_id));
        return JSONObject.toJSONString(new ResponseBean<>(
                this.userServiceImp.selectUserSimpleInfo(user.getStudent_id()),
                UnicomResponseEnums.SUCCESS));
    }
    @PostMapping(value = "update", produces = "application/json")
    public String update(@RequestBody JSONObject json){
        //插入跑步数据到mysql
        Record record = new Record(json);
        this.updateRunDataImp.notInvitedUpdate(record);

        //得到插入数据返回的id，并以 记录id－轨迹  存入redis
        int recordId = record.getId();
        JSONArray latLing = json.getJSONArray("lat_lng");
        this.recordServiceImp.insertLatLngToRedis(String.valueOf(recordId), json.getString("lat_lng"));

        //更新redis的个人和班级RSET集合（日周月总榜）
        this.updateRunDataImp.insertOnceRunDataToRedis(record);

        record.setLat_lng(latLing);
        return JSONObject.toJSONString(new ResponseBean<>(record,UnicomResponseEnums.SUCCESS));
    }
    @GetMapping(value = "sanzou/user/{student_id}/{page?}", produces = "application/json")
    public String getLatLngList(String student_id,String page){
        int num =0;
        JSONArray jsonArray = this.recordServiceImp.getLatLngList(student_id, page, num);
        return JSONObject.toJSONString(new RankResponseBean(jsonArray, UnicomResponseEnums.SUCCESS, num));
    }
    @GetMapping(value = "user/history/detail", produces = "application/json")
    public String getHistoryDetail(String id){
        Record record = this.recordServiceImp.getRecordById(id);
        return JSONObject.toJSONString(new ResponseBean<>(record, UnicomResponseEnums.SUCCESS));
    }
    @GetMapping(value = "sanzou/user/info/{student_id}", produces = "application/json")
    public String getUserOtherInfo(String student_id){
        UserOtherInfo userOtherInfo = this.userServiceImp.selectUserOtherInfo(student_id);
        return JSONObject.toJSONString(new ResponseBean<>(userOtherInfo, UnicomResponseEnums.SUCCESS));
    }
    @ResponseBody
    @PostMapping(value = "/upload", produces = "application/json")
    public String upload(HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest) { // 如果你现在是MultipartHttpServletRequest对象
            MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = mrequest.getFiles("file");
            Iterator<MultipartFile> iter = files.iterator();
            while (iter.hasNext()) {
                MultipartFile file = iter.next() ;
                if (file != null) { // 现在有文件上传
                    try {
                        // Get the file and save it somewhere
                        byte[] bytes = file.getBytes();
                        long fileName = System.currentTimeMillis();
                        Path path = Paths.get(this.config.getPhoto() +"/"+ fileName+".jpg");
                        String url = this.config.getPhotoUrl()+"/"+fileName+".jpg";

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
