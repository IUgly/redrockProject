package team.redrock.running.service;

import org.springframework.beans.factory.annotation.Autowired;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.service.serviceImp.UserServiceImp;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.Record;
import team.redrock.running.vo.User;

//@Service
public class RecordServiceImp implements IRecordService{
    @Autowired
    private RecordDao recordDao;
    @Autowired
    private UserServiceImp userServiceImp;
    @Override
    public void addRecord(String table, Record record) {
        User user = userServiceImp.selectUserInfo(record.getStudent_id());
        RankInfo rankInfo = new RankInfo(record);
        rankInfo.setStudent_id(user.getStudent_id());
        if (table.equals("distance")){
            recordDao.insertDistanceRecord(record);
            recordDao.updateDayDistanceScoreToStuMysql(rankInfo);
            recordDao.updateDayDistanceScoreToClaMysql(rankInfo);
        }else if (table.equals("invitation")){
            recordDao.updateDayInviteScoreToStuMysql(rankInfo);
            recordDao.insertDistanceRecord(record);
            recordDao.updateDayDistanceScoreToStuMysql(rankInfo);
            recordDao.updateDayDistanceScoreToClaMysql(rankInfo);
        }
    }
}
