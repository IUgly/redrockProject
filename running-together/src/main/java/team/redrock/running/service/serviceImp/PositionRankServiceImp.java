package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.service.PositionRankInterface;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.User;

import java.util.Set;

@Service
@Component
public class PositionRankServiceImp  implements PositionRankInterface {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    public static final String SCORE_RANK = "dayRank";
    @Override
    public RankInfo dayNumRankByStudentId(User user) {
        Long rankNum = this.redisTemplate.opsForZSet().reverseRank(SCORE_RANK, user.toString());
        RankInfo dayStuNumRank = new RankInfo(user);
        dayStuNumRank.setRank(rankNum);
        Set<String> preRankNum = this.redisTemplate.opsForZSet().reverseRange(SCORE_RANK,rankNum, rankNum);
        dayStuNumRank.setPrev_difference(JSON.toJSONString(preRankNum));//??
        dayStuNumRank.setTotal(this.redisTemplate.opsForZSet().zCard(SCORE_RANK));//??
        return dayStuNumRank;
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
