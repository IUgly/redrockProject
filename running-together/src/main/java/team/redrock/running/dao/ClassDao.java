package team.redrock.running.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.ClassRank;

@Mapper
@Component
public interface ClassDao {
    @Select("select * from class_distance_rank where class_id = #{class_id}")
    ClassRank selectClassByClassId(String class_id);

    @Insert("replace into class_distance_rank set day_distance=day_distance+#{distance},month_distance=month_distance+#{distance},week_distance=week_distance+#{distance},all_distance=all_distance+#{distance},college=#{college} where class_id =#{classId}")
    Boolean insertClassDistance(String distance, String college, String classId);
}
