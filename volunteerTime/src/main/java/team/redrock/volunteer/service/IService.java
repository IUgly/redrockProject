package team.redrock.volunteer.service;

import team.redrock.volunteer.vo.Record;
import team.redrock.volunteer.vo.User;

import java.util.List;

/**
 * @author kuangjunlin
 */
public interface IService {
    /**
     * fetch data by rule id
     *
     * @param record 志愿记录
     * @return bool
     */
    public boolean insertRecord(Record record);

    /**
     * fetch data by rule id
     *
     * @param uid 学号
     * @return bool
     */
    public List<Record> selectRecordList(String uid);

    /**
     * fetch data by rule id
     *
     * @param user 用户
     * @return bool
     */
    public Boolean insertBind(User user);

    /**
     * fetch data by rule id
     *
     * @param uid 学号
     * @return 用户
     */
    public User selectUser(String uid);

    /**
     * fetch data by rule id
     *
     * @param user 用户
     * @return bool
     */
    public Boolean updateUser(User user);

    /**
     * fetch data by rule id
     *
     * @param uid 学号
     * @return bool
     */
    public Boolean deleteRecord(String uid);

}
