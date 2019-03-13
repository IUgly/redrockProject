package team.redrock.cheerleaders.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.redrock.cheerleaders.dao.Dao;

import java.text.SimpleDateFormat;


@Component
public class ScheduledTasks {
    @Autowired
    private Dao dao;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime(){
//        User user = new User();
//        user.setUsername(String.valueOf(dateFormat.format(new Date())));
//        this.dao.updateUser(user);
//        log.info("The time is now {}", dateFormat.format(new Date()));
    }

}
