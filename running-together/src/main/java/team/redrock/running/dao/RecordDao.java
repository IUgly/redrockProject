package team.redrock.running.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.InviteInfo;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.Record;

import java.util.List;

@Mapper
@Component
public interface RecordDao {

    @Insert("INSERT INTO distance_record " +
            "set student_id=#{student_id},begin_time=#{begin_time}," +
            "end_time=#{end_time},distance=#{distance},steps=#{steps}," +
            "lat_lng=#{lat_lng, typeHandler=team.redrock.running.bean.JsonTypeHandler}")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void insertDistanceRecord(Record record);

    @Results({@Result(column="lat_lng",property="lat_lng", typeHandler = team.redrock.running.bean.JsonTypeHandler.class)})
    @SelectProvider(type = RecordSQL.class, method = "latLngOrDistance")
    List<Record> selectDistanceRecordList(@Param("student_id") String student_id,
                                          @Param("page") Integer page,
                                          @Param("type") String type);

    @Results({@Result(column="lat_lng",property="lat_lng", typeHandler = team.redrock.running.bean.JsonTypeHandler.class)})
    @Select("select id,student_id,begin_time,end_time,distance,steps,date,lat_lng from distance_record where id = #{id}")
    Record selectDistanceRecordById(String id);

    @Insert("Insert into invited_record set invited_student_id = #{invited_studentId}, date=#{date}, passive_students= #{resultString}")
    @Options(useGeneratedKeys=true, keyProperty="invited_id", keyColumn="invited_id")
    void insertInvitedRecord(InviteInfo inviteInfo);

    @Results({@Result(column="passive_students",property="passive_students", typeHandler = team.redrock.running.bean.JsonTypeHandler.class)})
    @SelectProvider(type = RecordSQL.class, method = "latLngOrDistance")
    List<InviteInfo> selectInvitedRecordList(@Param("student_id") String student_id,
                                             @Param("page") Integer page,
                                             @Param("type") String type);

    /**
     * 查询mysql中有无对应学生或者班级 路程的排名信息，没有则插入  //on DUPLICATE key update
     * @param rankInfo
     * @return
     */
    @Insert("insert into student_distance_rank " +
            "set student_id=#{student_id},nickname=#{nickname}," +
            "days=#{distance},weekends=#{distance}," +
            "months=#{distance},alls=#{distance}," +
            "college=#{college},duration=#{duration},class_id=#{class_id} " +
            "on DUPLICATE key update " +
            "days = days + #{distance}," +
            "weekends = weekends + #{distance}," +
            "months = months +#{distance}," +
            "alls =alls +#{distance}")
    void updateDayDistanceScoreToStuMysql(RankInfo rankInfo);
    @Insert("insert into class_distance_rank set class_id=#{class_id}," +
            "days=#{distance},weekends=#{distance}," +
            "months=#{distance},alls=#{distance}," +
            "college=#{college},duration=#{duration} on DUPLICATE key " +
            "update days = days + #{distance}," +
            "weekends = weekends + #{distance}," +
            "months = months +#{distance},alls =alls+#{distance}")
    void updateDayDistanceScoreToClaMysql(RankInfo rankInfo);

    /**
     * 查询mysql中有无对应学生或者班级 邀约的排名信息，没有则插入  //on DUPLICATE key update
     * @param rankInfo
     * @return
     */
    @Insert("insert into student_invitation_rank " +
            "set student_id=#{student_id},nickname=#{nickname}," +
            "days= #{total},weekends =#{total}," +
            "months =#{total},alls =#{total}," +
            "college=#{college},class_id=#{class_id} on DUPLICATE key " +
            "update days =#{total}," +
            "weekends = weekends + #{total}," +
            "months = months +#{total}," +
            "alls = alls +#{total}")
    void updateDayInviteScoreToStuMysql(RankInfo rankInfo);

    @SelectProvider(type = TableSizeSQL.class, method = "tableSize")
    Integer recordSize(@Param("table") String table,
                       @Param("student_id") String student_id);

    @SelectProvider(type = RankSQL.class, method = "rankList")
    List<RankInfo> size(@Param("table") String table,
                 @Param("type") String type,
                 @Param("page") Integer page);

    @Delete("delete from invited_record where invited_id = #{invited_id}")
    void deleteInvitationRecord(String invited_id);
}
