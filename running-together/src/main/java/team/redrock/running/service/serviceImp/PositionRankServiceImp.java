package team.redrock.running.service.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.service.PositionRankInterface;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.User;

import java.util.Iterator;
import java.util.Set;

@Service
@Component
public class PositionRankServiceImp  implements PositionRankInterface {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    public static final String DaySCORE_RANK = "dayRankTEST";
    @Override
    public RankInfo dayNumRankByStudentId(User user) {
        RankInfo rankInfo = new RankInfo(user);
        Long rankNum = redisTemplate.opsForZSet().reverseRank(DaySCORE_RANK, user.getStudent_id());
        Double score = redisTemplate.opsForZSet().score(DaySCORE_RANK, user.getStudent_id());

        if (rankNum==0){
            rankInfo.setRank(rankNum+1);
            rankInfo.setPrev_difference("0");
            rankInfo.setTotal(score);
            return rankInfo;
        }
        Set<ZSetOperations.TypedTuple<String>> range = redisTemplate.opsForZSet().reverseRangeWithScores(DaySCORE_RANK, rankNum-1, rankNum-1);
        Iterator<ZSetOperations.TypedTuple<String>> it = range.iterator();
        while (it.hasNext()) {//求出和前一名的差距
            ZSetOperations.TypedTuple str = it.next();
            rankInfo.setPrev_difference(String.valueOf(score-str.getScore()));
        }
        rankInfo.setRank(rankNum+1);
        rankInfo.setTotal(score);

        return rankInfo;
    }

    @Override
    public RankInfo weekNumRankByStudentId(User user) {
        return null;
    }

    @Override
    public RankInfo monthNumRankByStudentId(User user) {
        return null;
    }
}
