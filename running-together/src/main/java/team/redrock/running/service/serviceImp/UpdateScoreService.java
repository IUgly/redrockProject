package team.redrock.running.service.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.Record;
import team.redrock.running.vo.User;

@Service
@Component
public class UpdateScoreService {
    @Autowired
    private RecordDao recordDao;
    @Autowired
    private UserServiceImp userServiceImp;

    //个人 班级排行榜  日周月总
    @Async
    public void notInvitedUpdate(Record record) {
        this.recordDao.insertDistanceRecord(record);
        String student_id = record.getStudent_id();
        User user = this.userServiceImp.selectUserInfo(student_id);
        record.setClass_id(user.getClass_id());
        record.setCollege(user.getCollege());

        RankInfo rankInfo = new RankInfo(record);
        rankInfo.setNickname(user.getNickname());

        this.recordDao.updateDayDistanceScoreToClaMysql(rankInfo);
        this.recordDao.updateDayDistanceScoreToStuMysql(rankInfo);

    }

}
