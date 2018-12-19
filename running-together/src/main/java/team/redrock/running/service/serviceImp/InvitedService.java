package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.dto.InvitationSend;
import team.redrock.running.vo.InviteInfo;
import team.redrock.running.vo.User;

import java.util.*;

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
//    @Async
    public void insertInvitationToRedis(InviteInfo inviteInfo){
        HashMap invitationHash = new HashMap();
        invitationHash.put(inviteInfo.getInvited_id(), inviteInfo);
        this.redisTemplate.opsForHash().putAll(INVITATION_REDIS, invitationHash);

        InviteInfo inviteInfo1 = (InviteInfo) this.redisTemplate.opsForHash().get(INVITATION_REDIS, inviteInfo.getInvited_id());
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
        this.recordDao.overInvited(inviteInfo);
    }
    public void startInvited(InviteInfo inviteInfo) {
        this.recordDao.insertInvitedRecord(inviteInfo);
    }

    public JsonArray getInvitedHistory(String student_id){
        List<InviteInfo> inviteInfoList = this.recordDao.selectInvitedRecordList(student_id);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(gson.toJson(inviteInfoList), JsonArray.class);
        return jsonArray;
    }
    public void needInvitedUserResult(InviteInfo inviteInfo,String student_id, String result){
        inviteInfo.getResult().put(student_id, result);
    }
    public JSONArray getInvitedResult(String student_id){
        User user = (User)this.redisTemplate.opsForHash().get(USER_REDIS, student_id);
        String invited_id = user.getInvitingNow();
        InviteInfo inviteInfo = (InviteInfo) this.redisTemplate.opsForHash().get(INVITATION_REDIS, invited_id);
        JSONArray jsonArray = new JSONArray();
        Map map = inviteInfo.getResult();
        Set<String> keys = map.keySet();
        for(String key:keys){
            JSONObject json = new JSONObject();
            json.put(key, map.get(key));
            jsonArray.add(json);
        }
        return jsonArray;
    }
    @Async
    public void cancelInvited(String invited_id){
        InviteInfo inviteInfo = (InviteInfo) this.redisTemplate.opsForHash().get(INVITATION_REDIS, invited_id);
        User user = (User) this.redisTemplate.opsForHash().get(USER_REDIS, inviteInfo.getInvited_studentId());
        user.setInvitingNow("");
    }

}
