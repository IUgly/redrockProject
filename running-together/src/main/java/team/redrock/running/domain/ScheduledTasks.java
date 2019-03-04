package team.redrock.running.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.redrock.running.service.serviceImp.ScheduledServiceImp;


@Component
public class ScheduledTasks {
    @Autowired
    private ScheduledServiceImp scheduledServiceImp;
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
