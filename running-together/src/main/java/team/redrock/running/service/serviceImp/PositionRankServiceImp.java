package team.redrock.running.service.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.User;

import java.util.Iterator;
import java.util.Set;

@Service
@Component
public class PositionRankServiceImp {
    //个人排行榜  日周月总
    public static final String STU_DAY_DISTANCE_RANK = "daysStuDistance000";
    public static final String STU_WEEK_DISTANCE_RANK = "weekendsStuDistance000";
    public static final String STU_MONTH_DISTANCE_RANK = "monthsStuDistance000";
    public static final String STU_All_DISTANCE_RANK = "allStuDistance000";

    //班级排行榜   日周月总
    public static final String CLA_DAY_DISTANCE_RANK = "dayClaDistance000";
    public static final String CLA_WEEK_DISTANCE_RANK = "weekendsClaDistance000";
    public static final String CLA_MONTH_DISTANCE_RANK = "monthsClaDistance000";
    public static final String CLA_ALL_DISTANCE_RANK = "allClaDistance000";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public RankInfo NumRankByStudentId(User user, String kindRank) {
        RankInfo rankInfo = new RankInfo(user);
        Long rankNum = redisTemplate.opsForZSet().reverseRank(kindRank, user.getStudent_id());
        Double score = redisTemplate.opsForZSet().score(kindRank, user.getStudent_id());

        if (rankNum==0){
            rankInfo.setRank(rankNum+1);
            rankInfo.setPrev_difference("0");
            rankInfo.setTotal(score);
            return rankInfo;
        }
        Set<ZSetOperations.TypedTuple<String>> range = redisTemplate.opsForZSet().
                reverseRangeWithScores(kindRank, rankNum-1, rankNum-1);
        Iterator<ZSetOperations.TypedTuple<String>> it = range.iterator();
        while (it.hasNext()) {//求出和前一名的差距
            ZSetOperations.TypedTuple str = it.next();
            rankInfo.setPrev_difference(String.valueOf(str.getScore()-score));
        }
        rankInfo.setRank(rankNum+1);
        rankInfo.setTotal(score);

        return rankInfo;
    }

}
