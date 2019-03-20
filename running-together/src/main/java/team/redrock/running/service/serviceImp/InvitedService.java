package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.bean.RankResponseBean;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.dao.ScheduledDao;
import team.redrock.running.dto.InvitationSend;
import team.redrock.running.enums.UnicomResponseEnums;
import team.redrock.running.util.SerializeUtil;
import team.redrock.running.vo.InviteInfo;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Component
public class InvitedService {
    public static final String INVITATION_REDIS = "InvitationRedis018";
    public static final String USER_REDIS = "User018";
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private RecordDao recordDao;
    @Autowired
    private ScheduledDao scheduledDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Async
    public void sendInvitations(String invitees, InviteInfo inviteInfo){
        InvitationSend invitationSend = new InvitationSend(inviteInfo);
        String[] student_ids = invitees.substring
                (1, invitees.length()-1).split(",");
        for (String ids: student_ids){
            User user = this.userServiceImp.selectUserInfo(ids);
            if (user!=null){
                user.enQueueInvitation(invitationSend);
                this.userServiceImp.updateUserInfoToRedis(user);
            }
        }
    }

    @Async
    public void invitationToMysql(String student_id, Double score){
        User user = this.userServiceImp.selectUserInfo(student_id);
        RankInfo rankInfo = new RankInfo(user);
        rankInfo.setTotal(score);

        this.scheduledDao.updateDayInviteScoreToStuMysql(rankInfo);
    }

    public void startInvited(InviteInfo inviteInfo) {
        this.recordDao.insertInvitedRecord(inviteInfo);
    }

    public String getInvitedHistory(String student_id, Integer page){
        List<InviteInfo> inviteInfoList = this.recordDao.selectInvitedRecordList(
                student_id,
                page,
                "invited_record");
        Integer this_pageSize = inviteInfoList.size();
        Gson gson = new Gson();
        Integer sum = this.recordDao.recordSize("invited_record", student_id);

        JSONArray jsonArray = JSONArray.parseArray(gson.toJson(inviteInfoList));
        return JSONObject.toJSONString(new RankResponseBean(jsonArray, UnicomResponseEnums.SUCCESS, sum, this_pageSize));
    }

    public InviteInfo getInvitedById(String invited_id){
        try {
            return (InviteInfo) SerializeUtil.deserialize(this.stringRedisTemplate.opsForValue().get(INVITATION_REDIS+invited_id));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public void updateInvitationInfo(InviteInfo inviteInfo){
        String invited_id = inviteInfo.getInvited_id();
        String strInvitedInfo = SerializeUtil.serialize(inviteInfo);
        this.stringRedisTemplate.opsForValue().set
                (INVITATION_REDIS+invited_id, strInvitedInfo);
    }

    //更新邀请人最近一次邀约的信息
    @Async
    public void updatesUserInvitationInfo(InviteInfo inviteInfo, User invite_user){
        updateInvitationInfo(inviteInfo);
        invite_user.setState("正在邀约");
        invite_user.setInvitingNow(inviteInfo.getInvited_id());
        this.userServiceImp.updateUserInfoToRedis(invite_user);
    }

    //得到最近一次邀约结果
    public JSONArray getInvitedResult(String student_id){
        User user = this.userServiceImp.selectUserInfo(student_id);
        String invited_id = user.getInvitingNow();
        if (invited_id == null){
            return null;
        }
        InviteInfo result = getInvitedById(invited_id);
        String resultString = result.getResultString();
        return JSONArray.parseArray(resultString);
    }
    @Async
    public void cancelInvited(String invited_id){
        InviteInfo inviteInfo = (InviteInfo) SerializeUtil.deserialize(this.stringRedisTemplate.opsForValue().get(INVITATION_REDIS+invited_id));
        Map<String,String> resultMap = inviteInfo.getResult();

        Map<String, String> result =
                        resultMap.entrySet().stream()
                        .filter(map -> map.getValue().equals("1"))
                        .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

        inviteInfo.setResult(result);

        this.stringRedisTemplate.opsForValue().set
                (INVITATION_REDIS+invited_id, SerializeUtil.serialize(inviteInfo));
    }
}
