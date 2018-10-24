package team.redrock.volunteer.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.redrock.volunteer.vo.Record;
import team.redrock.volunteer.vo.User;

import java.util.List;

/**
 * @author kuangjunlin
 */
@Mapper
@Component
public interface Dao {
    @Select("SELECT * FROM record WHERE uid = #{uid}")
    /**
     * fetch data by uid
     *
     * @param uid 学号
     * @return 志愿服务详情
     */
    List<Record> getRecordList(String uid);

    @Insert("INSERT into record(hours,content,start_time,title,addway,status,uid) value (#{hours},#{content},#{start_time},#{title},#{addWay},#{status},#{uid})")
    /**
     * fetch data by record
     *
     * @param record 每条志愿的信息
     * @return 是否成功
     */
    Boolean insertRecords(Record record);

    @Insert("Insert into id_account(uid, account, password) value (#{uid},#{account},#{password})")
    /**
     * 账号密码绑定学号
     *
     * @param user 用户
     * @return 是否成功
     */
    Boolean bindInsert(User user);

    @Select("Select * from id_account where uid = #{uid}")
    /**
     * 查询用户by学号
     *
     * @param uid 学号
     * @return 用户
     */
    User selectUser(String uid);

    @Update("update id_account set account=#{account},password=#{password} where uid = #{uid}")
    /**
     * 跟新用户信息
     *
     * @param user 用户信息
     * @return 是否成功
     */
    Boolean updateUser(User user);

    @Delete("delete from record where uid = #{uid}")
    /**
     * 删除用户信息
     *
     * @param uid 学号
     * @return 是否成功
     */
    Boolean deleteRecord(String uid);
}
