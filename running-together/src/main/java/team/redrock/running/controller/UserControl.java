package team.redrock.running.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.redrock.running.bean.RankResponseBean;
import team.redrock.running.bean.ResponseBean;
import team.redrock.running.enums.UnicomResponseEnums;
import team.redrock.running.service.serviceImp.PositionRankServiceImp;
import team.redrock.running.service.serviceImp.RecordServiceImp;
import team.redrock.running.service.serviceImp.UpdateRunDataImp;
import team.redrock.running.service.serviceImp.UserServiceImp;
import team.redrock.running.util.AbstractBaseController;
import team.redrock.running.vo.Record;
import team.redrock.running.vo.User;
import team.redrock.running.vo.UserOtherInfo;

@RestController
public class UserControl extends AbstractBaseController {
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private UpdateRunDataImp updateRunDataImp;
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

            System.out.println(this.userServiceImp.selectUserInfo(student_id));
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
        int num = 0;
        JSONArray jsonArray = this.recordServiceImp.getLatLngList(student_id, page, num);
        return JSONObject.toJSONString(new RankResponseBean(jsonArray, UnicomResponseEnums.SUCCESS, num));
    }
    @GetMapping(value = "sanzou/user/info/{student_id}", produces = "application/json")
    public String getUserOtherInfo(String student_id){
        UserOtherInfo userOtherInfo = this.userServiceImp.selectUserOtherInfo(student_id);
        return JSONObject.toJSONString(new ResponseBean<>(userOtherInfo, UnicomResponseEnums.SUCCESS));
    }
}
