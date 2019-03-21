package team.redrock.running.service.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.vo.InviteInfo;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.Record;
import team.redrock.running.vo.User;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@Component
public class UpdateScoreService {
    public static final String INVITATION_REDIS = "InvitationRedis018";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RecordDao recordDao;
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private InvitedService invitedService;

    //个人 班级排行榜  日周月总
    @Async
    public void notInvitedUpdate(Record record) {
        this.recordDao.insertDistanceRecord(record);
        String student_id = record.getStudent_id();
        User user = this.userServiceImp.selectUserInfo(student_id);
        record.setClass_id(user.getClass_id());
        record.setCollege(user.getCollege());

        RankInfo rankInfo = new RankInfo(record);
        rankInfo.setNickname(user.getNickname());

        this.recordDao.updateDayDistanceScoreToClaMysql(rankInfo);
        this.recordDao.updateDayDistanceScoreToStuMysql(rankInfo);

    }

//    @Async
    public void invitationUpdate(Record record){
        this.notInvitedUpdate(record);
        this.recordDao.insertDistanceRecord(record);
        String student_id = record.getStudent_id();
        User user = this.userServiceImp.selectUserInfo(student_id);
        record.setClass_id(user.getClass_id());
        record.setCollege(user.getCollege());

        RankInfo rankInfo = new RankInfo(record);
        rankInfo.setNickname(user.getNickname());

        this.recordDao.updateDayDistanceScoreToClaMysql(rankInfo);
        this.recordDao.updateDayDistanceScoreToStuMysql(rankInfo);


        String invited_id = record.getInvited_id();
        InviteInfo inviteInfo = invitedService.getInvitedById(invited_id);
        Map<String, String> map = inviteInfo.getResult();
        map.put(student_id, "5");
        inviteInfo.setResult(map);
        this.invitedService.updateInvitationInfo(inviteInfo);

        user.setInvitingNow("");
        this.userServiceImp.updateUserInfoToRedis(user);

        Map<String, String> exitUser =
                map.entrySet().stream()
                        .filter(m -> m.getValue().equals("1"))
                        .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        if (exitUser.size() == 0){
            this.stringRedisTemplate.delete(INVITATION_REDIS+invited_id);
        }
    }

}
