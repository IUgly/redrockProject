package team.redrock.running.service.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.Record;

@Service
@Component
public class UpdateRunDataImp {

    @Autowired
    private RecordDao recordDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public static final String SCORE_RANK = "dayRankTEST";

    @Async
    public void notInvitedUpdate(Record record) {
        this.recordDao.insertRecord(record);
    }

    @Async
    public void insertOnceDayRunToRedis(RankInfo rankInfo) {
//        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
//        ZSetOperations.TypedTuple typedTuple = rankInfo;
//        tuples.add(typedTuple);
//        this.redisTemplate.opsForZSet().add(SCORE_RANK, tuples);
        /**
         * 更新个人每日路程排行榜redis
         */
        this.redisTemplate.opsForZSet().incrementScore(SCORE_RANK, rankInfo.getStudent_id(), rankInfo.getScore());
    }
}
