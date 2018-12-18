package team.redrock.running.service.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.vo.InviteInfo;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.Record;
import team.redrock.running.vo.User;

@Service
@Component
public class UpdateScoreService {
    @Autowired
    private RecordDao recordDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private RecordServiceImp recordServiceImp;

    //个人路程排行榜  日周月总
    public static final String STU_DAY_DISTANCE_RANK = "daysStuDistance000";
    public static final String STU_WEEK_DISTANCE_RANK = "weekendsStuDistance000";
    public static final String STU_MONTH_DISTANCE_RANK = "monthsStuDistance000";
    public static final String STU_All_DISTANCE_RANK = "allStuDistance000";

    //班级路程排行榜   日周月总
    public static final String CLA_DAY_DISTANCE_RANK = "dayClaDistance000";
    public static final String CLA_WEEK_DISTANCE_RANK = "weekendsClaDistance000";
    public static final String CLA_MONTH_DISTANCE_RANK = "monthsClaDistance000";
    public static final String CLA_ALL_DISTANCE_RANK = "allClaDistance000";

    //个人邀约排行榜  日周月总
    public static final String STU_DAY_INVITATION_RANK = "daysStuInvited000";
    public static final String STU_WEEK_INVITATION_RANK = "weekendsStuInvited000";
    public static final String STU_MONTH_INVITATION_RANK = "monthsStuInvited000";
    public static final String STU_ALL_INVITATION_RANK = "allStuInvited000";

    //班级邀约排行榜   日周月总
    public static final String CLA_DAY_INVITATION_RANK = "dayClaDistance000";
    public static final String CLA_WEEK_INVITATION_RANK = "weekendsClaDistance000";
    public static final String CLA_MONTH_INVITATION_RANK = "monthsClaDistance000";
    public static final String CLA_ALL_INVITATION_RANK = "allClaDistance000";

    //班级排行榜  日周月总
    public void notInvitedUpdate(Record record) {
        this.recordDao.insertDistanceRecord(record);
    }
    @Async
    public void insertOnceRunDataToRedis(Record record) {
        RankInfo rankInfo = new RankInfo(record);
        String student_id = rankInfo.getStudent_id();
        double distance = rankInfo.getDistance();
        this.redisTemplate.opsForZSet().incrementScore(STU_DAY_DISTANCE_RANK, student_id, distance);
        this.redisTemplate.opsForZSet().incrementScore(STU_WEEK_DISTANCE_RANK, student_id, distance);
        this.redisTemplate.opsForZSet().incrementScore(STU_MONTH_DISTANCE_RANK, student_id, distance);
        this.redisTemplate.opsForZSet().incrementScore(STU_All_DISTANCE_RANK, student_id, distance);

        User user = this.userServiceImp.selectUserInfo(student_id);
        String class_id = user.getClass_id();
        this.redisTemplate.opsForZSet().incrementScore(CLA_DAY_DISTANCE_RANK, class_id, distance);
        this.redisTemplate.opsForZSet().incrementScore(CLA_WEEK_DISTANCE_RANK, class_id, distance);
        this.redisTemplate.opsForZSet().incrementScore(CLA_MONTH_DISTANCE_RANK, class_id, distance);
        this.redisTemplate.opsForZSet().incrementScore(CLA_ALL_DISTANCE_RANK, class_id, distance);
    }

    @Async
    public void insertOnceInvitedData(InviteInfo inviteInfo){
        String student_id = inviteInfo.getInvited_studentId();
        double score = inviteInfo.getDistance();
        this.redisTemplate.opsForHash().increment(STU_DAY_INVITATION_RANK, student_id, score);
        this.redisTemplate.opsForHash().increment(STU_WEEK_INVITATION_RANK, student_id, score);
        this.redisTemplate.opsForHash().increment(STU_MONTH_INVITATION_RANK, student_id, score);
        this.redisTemplate.opsForHash().increment(STU_ALL_INVITATION_RANK, student_id, score);
    }
}
