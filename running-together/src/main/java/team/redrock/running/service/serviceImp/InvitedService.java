package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.vo.InviteInfo;
import team.redrock.running.vo.User;

import java.util.HashMap;
import java.util.Iterator;
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
    public String selectUserState(String student_id){
        User user = this.userServiceImp.selectUserInfo(student_id);
        String state = user.getState();
        return state;
    }
    @Async
    public void insertInvitationToRedis(InviteInfo inviteInfo){
        HashMap invitationHash = new HashMap();
        invitationHash.put(inviteInfo.getInvited_id(), inviteInfo);
        this.redisTemplate.opsForHash().putAll(INVITATION_REDIS, invitationHash);
    }
    @Async
    public void sendInvitations(String invitees, User invite_user){
        String[] student_ids = invitees.split(",");
        for (String ids: student_ids){
            InviteInfo inviteInfo = new InviteInfo(invite_user, ids);
            User user = (User) this.redisTemplate.opsForHash().get(USER_REDIS, ids);
            user.enQueueInvitation(inviteInfo);
        }
    }
    @Async
    public void OverInvitation(String invited_id, InviteInfo inviteInfo){
        this.redisTemplate.opsForHash().delete(INVITATION_REDIS, invited_id);
        inviteInfo.setResult("END");
        this.recordDao.insertInvitedRecord(inviteInfo);
    }
    public JsonArray getInvitedHistory(String student_id){
        List<InviteInfo> inviteInfoList = this.recordDao.selectInvitedRecordList(student_id);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(gson.toJson(inviteInfoList), JsonArray.class);
        return jsonArray;
    }
    public void needInvitedUserResult(InviteInfo inviteInfo,String student_id, String result){
        User sendInvitedUser = (User) this.redisTemplate.opsForHash().get(INVITATION_REDIS, inviteInfo.getInvited_studentId());
        sendInvitedUser.getInvitingMap().get(student_id).setResult(result);
    }
    public JSONArray getInvitedResult(String student_id){
        User user = (User)this.redisTemplate.opsForHash().get(USER_REDIS, student_id);
        Map<String, InviteInfo> inviteInfoMap = user.getInvitingMap();
        JSONArray jsonArray = new JSONArray();
        Iterator iterator = inviteInfoMap.entrySet().iterator();
        while (iterator.hasNext()){
            jsonArray.add(iterator.next());
        }
        return jsonArray;
    }
    @Async
    public void cancelInvited(String invited_id){
        InviteInfo inviteInfo = (InviteInfo) this.redisTemplate.opsForHash().get(INVITATION_REDIS, invited_id);
        User user = (User) this.redisTemplate.opsForHash().get(USER_REDIS, inviteInfo.getInvited_studentId());
        user.getInvitingMap().clear();
    }

}
