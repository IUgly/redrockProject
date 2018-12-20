package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.dto.InvitationSend;
import team.redrock.running.vo.InviteInfo;
import team.redrock.running.vo.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private RecordDao recordDao;

    @Async
    public void insertInvitationToRedis(InviteInfo inviteInfo){
        HashMap invitationHash = new HashMap();
        invitationHash.put(inviteInfo.getInvited_id(), inviteInfo);
        this.redisTemplate.opsForHash().putAll(INVITATION_REDIS, invitationHash);
    }
    @Async
    public void sendInvitations(String invitees, InviteInfo inviteInfo){
        InvitationSend invitationSend = new InvitationSend(inviteInfo);
        String[] student_ids = invitees.substring
                (1, invitees.length()-1).split(",");
        for (String ids: student_ids){
            User user = this.userServiceImp.selectUserInfo(ids);
            user.enQueueInvitation(invitationSend);
            this.userServiceImp.insertUserToRedis(user.getStudent_id(), user);
        }
    }
    @Async
    public void OverInvitation(String invited_id, InviteInfo inviteInfo){
        this.redisTemplate.opsForHash().delete(INVITATION_REDIS, invited_id);
        inviteInfo.setState("END");
        JSONArray studentsJsonArr = new JSONArray();
        String[] passive_students = inviteInfo.getPassive_studentArray();
        for (String s: passive_students){
            JSONObject json = new JSONObject();
            json.put("student_id", s);
            studentsJsonArr.add(json);
        }
        inviteInfo.setPassive_students(studentsJsonArr);
        this.recordDao.overInvited(inviteInfo);
//        this.recordDao.addOneInvitedNum(inviteInfo.getInvited_studentId());
    }
    public void startInvited(InviteInfo inviteInfo) {
        this.recordDao.insertInvitedRecord(inviteInfo);
    }
    public JSONArray getInvitedHistory(String student_id){
        List<InviteInfo> inviteInfoList = this.recordDao.selectInvitedRecordList(student_id);
        Gson gson = new Gson();
        JSONArray jsonArray = JSONArray.parseArray(gson.toJson(inviteInfoList));
        return jsonArray;
    }

    public JSONArray getInvitedResult(String student_id){
        User user = (User)this.redisTemplate.opsForHash().get(USER_REDIS, student_id);
        String invited_id = user.getInvitingNow();
        InviteInfo inviteInfo = (InviteInfo) this.redisTemplate.opsForHash().get(INVITATION_REDIS, invited_id);
        JSONArray jsonArray = new JSONArray();
        Map<String, String> map = inviteInfo.getResult();
        for(String key:map.keySet()){
            JSONObject json = new JSONObject();
            json.put(key, map.get(key));
            jsonArray.add(json);
        }
        return jsonArray;
    }
    @Async
    public void cancelInvited(String invited_id){
        this.redisTemplate.opsForHash().delete(INVITATION_REDIS, invited_id);
        System.out.println(this.redisTemplate.opsForHash().get(INVITATION_REDIS, invited_id).toString());
    }

}
