package team.redrock.running.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import team.redrock.running.service.serviceImp.ScheduledServiceImp;

import java.text.SimpleDateFormat;
import java.util.Date;


//@Component
public class ScheduledTasks {
    @Autowired
    private ScheduledServiceImp scheduledServiceImp;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "0 30 23 * * ?")
    public void reportCurrentTime(){
        this.scheduledServiceImp.insertDayDistanceToWeekRank();
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

}
