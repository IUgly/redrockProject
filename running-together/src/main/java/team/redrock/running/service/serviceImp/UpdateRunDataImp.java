package team.redrock.running.service.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.Record;
import team.redrock.running.vo.User;

@Service
@Component
public class UpdateRunDataImp {
    public static final String LAT_LNG = "Lat_Lng";
    @Autowired
    private RecordDao recordDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private RecordServiceImp recordServiceImp;

    //个人排行榜  日周月总
    public static final String StuDayDistanceRank = "stuDayRankTEST";
    public static final String StuWeekDistanceRank = "stuWeekDistanceRank000";
    public static final String StuMonthDistanceRank = "stuMonthDistanceRank000";
    public static final String StuAllDistanceRank = "stuAllDistanceRank000";

    //班级排行榜   日周月总
    public static final String ClaDayDistanceRank = "claDayRankTEST";
    public static final String ClaWeekDistanceRank = "claWeekDistanceRank000";
    public static final String ClaMonthDistanceRank = "claMonthDistanceRank000";
    public static final String ClaAllDistanceRank = "claAllDistanceRank000";

    //班级排行榜  日周月总
    public void notInvitedUpdate(Record record) {
        this.recordDao.insertRecord(record);
        this.recordServiceImp.putRedisHash(record.getId(), record.getLat_lng().toString(), LAT_LNG);

    }

    @Async
    public void insertOnceRunDataToRedis(Record record) {
        RankInfo rankInfo = new RankInfo(record);
        String student_id = rankInfo.getStudent_id();
        double distance = rankInfo.getDistance();
        this.redisTemplate.opsForZSet().incrementScore(StuDayDistanceRank, student_id, distance);
        this.redisTemplate.opsForZSet().incrementScore(StuWeekDistanceRank, student_id, distance);
        this.redisTemplate.opsForZSet().incrementScore(StuMonthDistanceRank, student_id, distance);
        this.redisTemplate.opsForZSet().incrementScore(StuAllDistanceRank, student_id, distance);

        User user = this.userServiceImp.selectUserInfo(student_id);
        String class_id = user.getClass_id();
        this.redisTemplate.opsForZSet().incrementScore(ClaDayDistanceRank, class_id, distance);
        this.redisTemplate.opsForZSet().incrementScore(ClaWeekDistanceRank, class_id, distance);
        this.redisTemplate.opsForZSet().incrementScore(ClaMonthDistanceRank, class_id, distance);
        this.redisTemplate.opsForZSet().incrementScore(ClaAllDistanceRank, class_id, distance);
    }
}
