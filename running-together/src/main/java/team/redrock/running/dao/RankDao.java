package team.redrock.running.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.RankInfo;

import java.util.List;

@Mapper
@Component
public interface RankDao {
    @Select("select student_id,#{kind_rank} as total from rank")
    List<RankInfo> selectRank(String kind_rank);
}
