package team.redrock.running.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.redrock.running.service.serviceImp.ScheduledServiceImp;

import java.util.Arrays;


@Component
public class ScheduledTasks {

    public final static long Three_Minute =  3*60 * 1000;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ScheduledServiceImp scheduledServiceImp;

    @Scheduled(fixedRate = Three_Minute)
    public void updateDayRunningData(){
        String[] ranks = {"student_distance_rank","student_invitation_rank","class_distance_rank"};

        Arrays.stream(ranks).forEach(r -> this.redisTemplate.delete(r));
        Arrays.stream(ranks).forEach(r -> this.redisTemplate.delete(r + "days"));
    }
    /**
     *  每周周日 周路程归零。
     */
    @Scheduled(cron = "0 31 23 ? * SUN")
    public void updateWeekDistance(){
        this.scheduledServiceImp.updateWeek();
    }
    /**
     *  每月月底月路程插入总路程，月路程归零
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void updateMonthDistance(){
        this.scheduledServiceImp.updateMonth();
    }
}
