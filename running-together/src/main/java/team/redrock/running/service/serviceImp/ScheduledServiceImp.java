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

    //个人路程排行榜  日周月总
    public static final String STU_DAY_DISTANCE_RANK = "daysStuDistance000";
    public static final String STU_WEEK_DISTANCE_RANK = "weekendsStuDistance000";
    public static final String STU_MONTH_DISTANCE_RANK = "monthsStuDistance000";
    public static final String STU_All_DISTANCE_RANK = "allStuDistance000";

    //班级路程排行榜   日周月总
    public static final String CLA_DAY_DISTANCE_RANK = "daysClaDistance000";
    public static final String CLA_WEEK_DISTANCE_RANK = "weekendsClaDistance000";
    public static final String CLA_MONTH_DISTANCE_RANK = "monthsClaDistance000";

    //个人邀约排行榜  日周月总
    public static final String STU_DAY_INVITATION_RANK = "daysStuInvited000";
    public static final String STU_WEEK_INVITATION_RANK = "weekendsStuInvited000";
    public static final String STU_MONTH_INVITATION_RANK = "monthsStuInvited000";

    @Autowired
    private ScheduledDao scheduledDao;
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    /**
     * 每天23:30从redis中一一取出当日跑步数据，更新mysql中个人排行表，和班级排行表
     */
    public void insertDayDistanceToWeekRank() {
        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = this.redisTemplate.opsForZSet().reverseRangeWithScores
                (STU_DAY_DISTANCE_RANK, 0, this.redisTemplate.opsForZSet().zCard(STU_DAY_DISTANCE_RANK));
        Iterator<ZSetOperations.TypedTuple<String>> it = rangeWithScores.iterator();

        while (it.hasNext()) {
            ZSetOperations.TypedTuple str = it.next();
            User user = this.userServiceImp.selectUserInfo(str.getValue().toString());
            RankInfo rankInfo = new RankInfo(user);
            rankInfo.setDistance(str.getScore());

            this.scheduledDao.updateDayDistanceScoreToStuMysql(rankInfo);
            this.scheduledDao.updateDayDistanceScoreToClaMysql(rankInfo);

            //执行完毕 置零
            this.redisTemplate.opsForZSet().add(STU_DAY_DISTANCE_RANK, user.getStudent_id(), 0);
        }
    }
    /**
     * 每天23:30从redis中一一取出当日邀约数据，更新mysql中个人 邀约排行表
     */
    public void insertDayInvitedToWeekRank() {
        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = this.redisTemplate.opsForZSet().reverseRangeWithScores
                (STU_DAY_INVITATION_RANK, 0, this.redisTemplate.opsForZSet().zCard(STU_DAY_INVITATION_RANK));
        Iterator<ZSetOperations.TypedTuple<String>> it = rangeWithScores.iterator();

        while (it.hasNext()) {
            ZSetOperations.TypedTuple str = it.next();
            User user = this.userServiceImp.selectUserInfo(str.getValue().toString());
            RankInfo rankInfo = new RankInfo(user);
            rankInfo.setTotal(str.getScore());

            this.scheduledDao.updateDayInviteScoreToStuMysql(rankInfo);

            //执行完毕 置零
            this.redisTemplate.opsForZSet().add(STU_DAY_INVITATION_RANK, user.getStudent_id(), 0);
        }
    }
    /**
     * 每周末23:35 周数据归零
     */
    public void updateWeek(){
        this.redisTemplate.opsForZSet().getOperations().delete(STU_WEEK_DISTANCE_RANK);
        this.redisTemplate.opsForZSet().getOperations().delete(STU_WEEK_INVITATION_RANK);
        this.redisTemplate.opsForZSet().getOperations().delete(CLA_WEEK_DISTANCE_RANK);

        this.scheduledDao.timingUpdateWeekDistanceScore();
        this.scheduledDao.timingUpdateWeekInvitedScore();
    }
    /**
     *  每月最后一天 23:40  月数据归零
     */
    public void updateMonth(){
        this.redisTemplate.opsForZSet().getOperations().delete(STU_MONTH_DISTANCE_RANK);
        this.redisTemplate.opsForZSet().getOperations().delete(STU_MONTH_INVITATION_RANK);
        this.redisTemplate.opsForZSet().getOperations().delete(CLA_MONTH_DISTANCE_RANK);

        this.scheduledDao.timingUpdateMonthDistanceScore();
        this.scheduledDao.timingUpdateMonthInvitedScore();
    }
}
