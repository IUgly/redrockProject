package team.redrock.volunteer.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import team.redrock.volunteer.service.impl.IServiceImp;
import team.redrock.volunteer.util.AbstractBaseController;
import team.redrock.volunteer.util.ReptileUtil;
import team.redrock.volunteer.util.Util;
import team.redrock.volunteer.vo.Record;
import team.redrock.volunteer.vo.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author kuangjunlin
 */
@RestController
public class Control extends AbstractBaseController {
    @Autowired
    private IServiceImp iServiceImp;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping(value = "/select", produces = "application/json;charset=UTF-8")
    public String Record(String uid) throws Exception {

        String resp = this.redisTemplate.opsForValue().get(uid);
        User user = this.iServiceImp.selectUser(uid);
        if (user==null){
            return Util.assembling(-2, "该学号还没有绑定志愿者账号", "");
        }
        if (resp!=null){
            return resp;
        }
        String code = Util.login(user.getAccount(), user.getPassword());
        if (!"0".equals(code)){
            return Util.assembling(3, "该志愿者账号密码被修改，请重新绑定", "");
        }
        List<Record> recordList =  ReptileUtil.detail(user.getAccount(), user.getPassword());

        if (recordList==null){
            recordList = this.iServiceImp.selectRecordList(uid);
        }else {
            this.iServiceImp.deleteRecord(uid);
            for (int i = 0; i < recordList.size(); i++) {
                Record record = recordList.get(i);
                record.setUid(uid);
                this.iServiceImp.insertRecord(record);
            }
        }
        double allHours = 0.0;
        for (int i=0; i<recordList.size(); i++){
            Record record = recordList.get(i);

            allHours = allHours+Double.parseDouble(record.getHours());
        }
        JsonArray jsonArray = new Gson().toJsonTree
                (recordList, new TypeToken<List<Record>>() {}.getType()).getAsJsonArray();
        this.redisTemplate.opsForValue().set(uid, Util.message("0", "success", allHours, jsonArray));
        this.redisTemplate.expire(uid, 604800, TimeUnit.SECONDS);

        return Util.message("0", "success", allHours, jsonArray);
    }
    @PostMapping(value = "/binding", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String binding(String uid, String account, String password) throws Exception {
        User user = new User(uid, account, password);
        String code = Util.login(user.getAccount(), user.getPassword());
        if ("0".equals(code)){
            if (this.iServiceImp.selectUser(user.getUid())==null){
                this.iServiceImp.insertBind(user);
            }else {
                this.iServiceImp.updateUser(user);
            }
            return Util.assembling(0,"success", "");
        }else {
            return Util.assembling(-1, "志愿者账号密码错误","");
        }
    }
}