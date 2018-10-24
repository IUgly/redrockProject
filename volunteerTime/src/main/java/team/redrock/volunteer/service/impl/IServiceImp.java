package team.redrock.volunteer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.redrock.volunteer.dao.Dao;
import team.redrock.volunteer.service.IService;
import team.redrock.volunteer.vo.Record;
import team.redrock.volunteer.vo.User;

import java.util.List;

/**
 * @author kuangjunlin
 */
@Service
public class IServiceImp implements IService {

    @Autowired
    private Dao dao;
    @Override
    public boolean insertRecord(Record record) {
        return this.dao.insertRecords(record);
    }

    @Override
    public List<Record> selectRecordList(String uid) {
        return this.dao.getRecordList(uid);
    }

    @Override
    public Boolean insertBind(User user) {
        return this.dao.bindInsert(user);
    }

    @Override
    public User selectUser(String uid) {
        return this.dao.selectUser(uid);
    }

    @Override
    public Boolean updateUser(User user) {
        return this.dao.updateUser(user);
    }

    @Override
    public Boolean deleteRecord(String uid) {
        return this.dao.deleteRecord(uid);
    }
}
