package team.redrock.running.service.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.ClassDao;
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
    private ClassDao classDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserServiceImp userServiceImp;

    //个人路程排行榜  日周月总
    public static final String STU_DAY_DISTANCE_RANK = "daysStuDistance002";
    public static final String STU_WEEK_DISTANCE_RANK = "weekendsStuDistance002";
    public static final String STU_MONTH_DISTANCE_RANK = "monthsStuDistance002";
    public static final String STU_All_DISTANCE_RANK = "allStuDistance002";

    //班级路程排行榜   日周月总
    public static final String CLA_DAY_DISTANCE_RANK = "daysClaDistance002";
    public static final String CLA_WEEK_DISTANCE_RANK = "weekendsClaDistance002";
    public static final String CLA_MONTH_DISTANCE_RANK = "monthsClaDistance002";
    public static final String CLA_ALL_DISTANCE_RANK = "allClaDistance002";

    //个人邀约排行榜  日周月总
    public static final String STU_DAY_INVITATION_RANK = "daysStuInvited002";
    public static final String STU_WEEK_INVITATION_RANK = "weekendsStuInvited002";
    public static final String STU_MONTH_INVITATION_RANK = "monthsStuInvited002";
    public static final String STU_ALL_INVITATION_RANK = "allStuInvited002";

    //个人 班级排行榜  日周月总
    @Async
    public void notInvitedUpdate(Record record) {
        this.recordDao.insertDistanceRecord(record);
        String student_id = record.getStudent_id();
        User user = this.userServiceImp.selectUserInfo(student_id);
        record.setClass_id(user.getClass_id());
        record.setCollege(user.getCollege());

        if (this.classDao.selectClassById(record)!=null){
            this.classDao.updateClassDistance(record);
        }else {
            this.classDao.insertClassDistance(record);
        }
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
    /**
     * 更新个人邀约排行榜 日周月总
     * @param inviteInfo
     */
    @Async
    public void insertOnceInvitedDataToRedis(InviteInfo inviteInfo){
        double score = inviteInfo.getDistance()*inviteInfo.getSuccessInvitedPersonNum();
        String invited_student_id = inviteInfo.getInvited_studentId();

        this.redisTemplate.opsForZSet().incrementScore(STU_DAY_INVITATION_RANK, invited_student_id, score);
        this.redisTemplate.opsForZSet().incrementScore(STU_WEEK_INVITATION_RANK, invited_student_id, score);
        this.redisTemplate.opsForZSet().incrementScore(STU_MONTH_INVITATION_RANK, invited_student_id, score);
        this.redisTemplate.opsForZSet().incrementScore(STU_ALL_INVITATION_RANK, invited_student_id, score);
    }

    public void deleteRedis(){
        this.redisTemplate.opsForZSet().getOperations().delete(STU_DAY_DISTANCE_RANK);
        this.redisTemplate.opsForZSet().getOperations().delete(STU_WEEK_DISTANCE_RANK);
        this.redisTemplate.opsForZSet().getOperations().delete(STU_MONTH_DISTANCE_RANK);
        this.redisTemplate.opsForZSet().getOperations().delete(STU_All_DISTANCE_RANK);

        this.redisTemplate.opsForZSet().getOperations().delete(CLA_DAY_DISTANCE_RANK);
        this.redisTemplate.opsForZSet().getOperations().delete(CLA_WEEK_DISTANCE_RANK);
        this.redisTemplate.opsForZSet().getOperations().delete(CLA_MONTH_DISTANCE_RANK);
        this.redisTemplate.opsForZSet().getOperations().delete(CLA_ALL_DISTANCE_RANK);

        this.redisTemplate.opsForZSet().getOperations().delete(STU_DAY_INVITATION_RANK);
        this.redisTemplate.opsForZSet().getOperations().delete(STU_WEEK_INVITATION_RANK);
        this.redisTemplate.opsForZSet().getOperations().delete(STU_MONTH_INVITATION_RANK);
        this.redisTemplate.opsForZSet().getOperations().delete(STU_ALL_INVITATION_RANK);
    }
}
