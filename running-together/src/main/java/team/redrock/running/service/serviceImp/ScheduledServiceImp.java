package team.redrock.running.service.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.ScheduledDao;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.User;

import java.util.Iterator;
import java.util.Set;

@Service
@Component
public class ScheduledServiceImp {
    public static final String SCORE_RANK = "dayRankTEST";
    @Autowired
    private ScheduledDao scheduledDao;
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    public void insertDayDistanceToWeekRank() {
        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = this.redisTemplate.opsForZSet().reverseRangeWithScores
                (SCORE_RANK, 0, this.redisTemplate.opsForZSet().zCard(SCORE_RANK));
        Iterator<ZSetOperations.TypedTuple<String>> it = rangeWithScores.iterator();

        while (it.hasNext()) {
            ZSetOperations.TypedTuple str = it.next();
            User user = this.userServiceImp.selectUserInfo(str.getValue().toString());
            RankInfo rankInfo = new RankInfo(user);
            rankInfo.setDistance(str.getScore());
            this.scheduledDao.insertRankInfoToMysql(rankInfo);
        }
    }
}
