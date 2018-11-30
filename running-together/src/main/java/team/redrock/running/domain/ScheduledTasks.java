package team.redrock.running.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import team.redrock.running.dao.RankDao;
import team.redrock.running.service.serviceImp.RankListServerImp;
import team.redrock.running.service.serviceImp.ScheduledServiceImp;
import team.redrock.running.service.serviceImp.UserServiceImp;
import team.redrock.running.vo.RankInfo;

import java.util.List;


//@Component
public class ScheduledTasks {
    public static final String weekDistanceRank = "weekDistanceRank000";
    public static final String monthDistanceRank = "monthDistanceRank000";
    public static final String allDistanceRank = "allDistanceRank000";
    @Autowired
    private ScheduledServiceImp scheduledServiceImp;
    @Autowired
    private RankDao rankDao;
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private RankListServerImp rankListServerImp;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    /**
     * 每天23:30定时每日路程，插入到rank表
     */
    @Scheduled(cron = "0 30 23 * * ?")
    public void reportCurrentTime(){
        this.scheduledServiceImp.insertDayDistanceToWeekRank();
    }
    /**
     *  每周周日周路程插入月和总路程，周路程归零。
     */
    @Scheduled(cron = "0 31 23 ? * SUN")
    public void updateWeekDistance(){
        this.scheduledServiceImp.updateWeekDistanceToMysql();
    }

    /**
     *  每月月底月路程插入总路程，月路程归零
     */
    @Scheduled(cron = "0 32 23 L * ?")
    public void updateMonthDistance(){
        this.scheduledServiceImp.updateMonthDistanceToMysql();
    }

    /**
     * 从mysql更新周数据到redis
     */
    @Scheduled(cron = "0 40 23 * * ?")
    public void updateWeekDistanceToRedis(){
        List<RankInfo> rankInfoList = this.rankDao.selectRank(weekDistanceRank);
        for (int i=0; i<rankInfoList.size(); i++){
            RankInfo rankInfo = rankInfoList.get(i);
            this.redisTemplate.opsForZSet().incrementScore(weekDistanceRank, rankInfo.getStudent_id(), rankInfo.getTotal());
        }
    }

    /**
     *  从mysql更新月数据到redis
     */
    @Scheduled(cron = "0 41 23 * * ?")
    public void updateMonthDistanceToRedis(){
        List<RankInfo> rankInfoList = this.rankDao.selectRank(monthDistanceRank);
        for (int i=0; i<rankInfoList.size(); i++){
            RankInfo rankInfo = rankInfoList.get(i);
            this.redisTemplate.opsForZSet().incrementScore(monthDistanceRank, rankInfo.getStudent_id(), rankInfo.getTotal());
        }
    }

    /**
     *  从mysql更新总数据到redis
     */
    @Scheduled(cron = "0 42 23 * * ?")
    public void updateAllDistanceToRedis(){
        List<RankInfo> rankInfoList = this.rankDao.selectRank(allDistanceRank);
        for (int i=0; i<rankInfoList.size(); i++){
            RankInfo rankInfo = rankInfoList.get(i);
            this.redisTemplate.opsForZSet().incrementScore(allDistanceRank, rankInfo.getStudent_id(), rankInfo.getTotal());
        }
    }
}
