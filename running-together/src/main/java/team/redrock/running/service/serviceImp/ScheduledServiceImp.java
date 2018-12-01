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
    public static final String DaySCORE_RANK = "dayRankTEST";
    @Autowired
    private ScheduledDao scheduledDao;
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 每天从redis中一一取出当日跑步数据，更新mysql中个人排行表，和班级排行表
     */
    public void insertDayDistanceToWeekRank() {
        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = this.redisTemplate.opsForZSet().reverseRangeWithScores
                (DaySCORE_RANK, 0, this.redisTemplate.opsForZSet().zCard(DaySCORE_RANK));
        Iterator<ZSetOperations.TypedTuple<String>> it = rangeWithScores.iterator();

        while (it.hasNext()) {
            ZSetOperations.TypedTuple str = it.next();
            User user = this.userServiceImp.selectUserInfo(str.getValue().toString());
            RankInfo rankInfo = new RankInfo(user);
            rankInfo.setDistance(str.getScore());

            if (this.scheduledDao.selectStuRankInfo(rankInfo)==null){
                this.scheduledDao.insertStuRankInfoToMysql(rankInfo);
            }
            if (this.scheduledDao.selectStuRankInfo(rankInfo)==null){
                this.scheduledDao.insertClaRankInfoToMysql(rankInfo);
            }

            this.scheduledDao.updateStuScore(rankInfo);

        }
    }
    /**
     * 每周末23:40 周数据归零
     */
    public void updateWeekDistance(){
        this.scheduledDao.timingUpdateWeekScore("student_rank");
        this.scheduledDao.timingUpdateWeekScore("class_rank");
    }
    /**
     *  每月最后一天 23:40  月数据归零
     */
    public void updateMonthDistance(){
        this.scheduledDao.timingUpdateMonthScore("student_rank");
        this.scheduledDao.timingUpdateMonthScore("class_rank");
    }
}
