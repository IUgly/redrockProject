package team.redrock.running.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.ClassRank;
import team.redrock.running.vo.Record;

@Mapper
@Component
public interface ClassDao {
    @Select("select * from class_distance_rank where class_id = #{class_id}")
    ClassRank selectClassByClassId(String class_id);

    @Update("update class_distance_rank set day_distance=day_distance+#{distance},month_distance=month_distance+#{distance},week_distance=week_distance+#{distance},all_distance=all_distance+#{distance} where class_id =#{class_id}")
    void updateClassDistance(Record record);

    @Insert("insert into class_distance_rank set day_distance=#{distance},month_distance=#{distance},week_distance=#{distance},all_distance=#{distance},college=#{college},class_id =#{class_id}")
    void insertClassDistance(Record record);

    @Select("select * from class_distance_rank where class_id = #{class_id}")
    ClassRank selectClassById(Record record);
}
