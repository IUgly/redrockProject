package team.redrock.running.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.Record;

import java.util.List;

@Mapper
@Component
public interface RecordDao {

    @Insert("INSERT INTO record set student_id=#{student_id},begin_time=#{begin_time},end_time=#{end_time},distance=#{distance},steps=#{steps},date=#{date},lat_lng=#{lat_lng, typeHandler=team.redrock.running.bean.JsonTypeHandler}")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void insertRecord(Record record);

    @Select("select * from record where student_id = #{student_id}")
    List<Record> selectRecordList(String student_id);

    @Select("select * from record where id = #{id}")
    Record selectRecordById(String id);

}
