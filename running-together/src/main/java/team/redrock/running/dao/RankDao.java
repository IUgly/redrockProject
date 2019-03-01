package team.redrock.running.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.RankInfo;

import java.util.List;

@Mapper
@Component
public interface RankDao {
    @Select("select student_id,day_distance as total from student_distance_rank")
    List<RankInfo> selectStuDayRank();
    @Select("select student_id,week_distance as total from student_distance_rank")
    List<RankInfo> selectStuWeekRank();
    @Select("select student_id,month_distance as total from student_distance_rank")
    List<RankInfo> selectStuMonthRank();
    @Select("select student_id,all_distance as total from student_distance_rank")
    List<RankInfo> selectStuAllRank();

    @SelectProvider(type = RankSQL.class, method = "rankList")
    List<RankInfo> rankList(@Param("type") String type, @Param("table") String table,
                            @Param("page") Integer page);

    @SelectProvider(type = RankSQL.class, method = "rankNum")
    Integer rankNum(@Param("table") String table);

    @SelectProvider(type = RankSQL.class, method = "rankPlace")
    RankInfo rankPlace (@Param("type") String type, @Param("table") String table,
                        @Param("id") String id);

}
