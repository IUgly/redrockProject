package team.redrock.running.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.ClassRank;

@Mapper
@Component
public interface ClassDao {
    @Select("select * from class_distance_rank where class_id = #{class_id}")
    ClassRank selectClassByClassId(String class_id);
    
}
