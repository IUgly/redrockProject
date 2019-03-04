package team.redrock.running.service.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.ScheduledDao;

@Service
@Component
public class ScheduledServiceImp {

    @Autowired
    private ScheduledDao scheduledDao;
    /**
     * 每周末23:35 周数据归零
     */
    public void updateWeek(){

        this.scheduledDao.timingUpdateWeekDistanceScore();
        this.scheduledDao.timingUpdateWeekDistanceScoreClass();
        this.scheduledDao.timingUpdateWeekInvitation();
    }
    /**
     *  每月最后一天 23:40  月数据归零
     */
    public void updateMonth(){

        this.scheduledDao.timingUpdateMonthDistanceScore();
        this.scheduledDao.timingUpdateMonthDistanceScoreClass();
        this.scheduledDao.timingUpdateMonthInvitation();
    }

}
