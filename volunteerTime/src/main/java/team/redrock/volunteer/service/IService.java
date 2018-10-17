package team.redrock.volunteer.service;

import team.redrock.volunteer.vo.Record;
import team.redrock.volunteer.vo.User;

import java.util.List;

public interface IService {
    public boolean insertRecord(Record record);

    public List<Record> selectRecordList(String uid);

    public Boolean insertBind(User user);

    public User selectUser(String uid);

    public Boolean updateUser(User user);

    public String login(String account, String password);

    public Boolean deleteRecord(String uid);

}
