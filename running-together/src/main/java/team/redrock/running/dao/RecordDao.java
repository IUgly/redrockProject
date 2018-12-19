package team.redrock.running.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.InviteInfo;
import team.redrock.running.vo.Record;

import java.util.List;

@Mapper
@Component
public interface RecordDao {

    @Insert("INSERT INTO distance_record set student_id=#{student_id},begin_time=#{begin_time},end_time=#{end_time},distance=#{distance},steps=#{steps},date=#{date},lat_lng=#{lat_lng, typeHandler=team.redrock.running.bean.JsonTypeHandler}")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void insertDistanceRecord(Record record);

    @Select("select * from distance_record where student_id = #{student_id}")
    @Results({@Result(column="lat_lng",property="lat_lng", typeHandler = team.redrock.running.bean.JsonTypeHandler.class)})
    List<Record> selectDistanceRecordList(String student_id);

    @Results({@Result(column="lat_lng",property="lat_lng", typeHandler = team.redrock.running.bean.JsonTypeHandler.class)})
    @Select("select id,student_id,begin_time,end_time,distance,steps,date,lat_lng from distance_record where id = #{id}")
    Record selectDistanceRecordById(String id);

    @Insert("Insert into invited_record set invited_student_id = #{invited_studentId}, date=#{date}")
    @Options(useGeneratedKeys=true, keyProperty="invited_id", keyColumn="invited_id")
    void insertInvitedRecord(InviteInfo inviteInfo);

    @Results({@Result(column="passive_students",property="passive_students", typeHandler = team.redrock.running.bean.JsonTypeHandler.class)})
    @Select("select distance,date,passive_students,invited_id from invited_record where invited_student_id=#{student_id}")
    List<InviteInfo> selectInvitedRecordList(String student_id);

    @Update("update invited_record set distance=#{distance},state=#{state},passive_students=#{student_id, typeHandler=team.redrock.running.bean.JsonTypeHandler} where invited_id=#{invited_id}")
    void overInvited(InviteInfo inviteInfo);
}
