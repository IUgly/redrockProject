package team.redrock.running.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.RankInfo;

import java.util.List;

@Mapper
@Component
public interface RankDao {
    @Select("select student_id,day_distance as total from student_rank")
    List<RankInfo> selectStuDayRank();
    @Select("select student_id,week_distance as total from student_rank")
    List<RankInfo> selectStuWeekRank();
    @Select("select student_id,month_distance as total from student_rank")
    List<RankInfo> selectStuMonthRank();
    @Select("select student_id,all_distance as total from student_rank")
    List<RankInfo> selectStuAllRank();

    @Select("select class_id,day_distance as total from class_rank")
    List<RankInfo> selectClaDayRank();
    @Select("select class_id,week_distance as total from class_rank")
    List<RankInfo> selectClaWeekRank();
    @Select("select class_id,month_distance as total from class_rank")
    List<RankInfo> selectClaMonthRank();
    @Select("select class_id,day_distance as total from class_rank")
    List<RankInfo> selectClaAllRank();

}
