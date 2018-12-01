package team.redrock.running.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.RankInfo;

@Mapper
@Component
public interface ScheduledDao {
    /**
     *  路程信息从redis更新到mysql
     * @param rankInfo 当日某用户排名信息
     */
    @Update("update student_rank set day_distance=#{distance},week_distance = week_distance + #{distance},month_distance=month_distance+#{distance},all_distance=all_distance+#{distance} where student_id = #{student_id}")
    void updateStuScore(RankInfo rankInfo);

    @Update("update class_rank set day_distance=#{distance},week_distance = week_distance + #{distance},month_distance=month_distance+#{distance},all_distance=all_distance+#{distance} where class_id = #{class_id}")
    void updateClaScore(RankInfo rankInfo);
    /**
     *  每周定时更新周路程到月路程，总路程。周路程清零
     */
    @Update("update #{kind_rank} set week_distance=0")
    void timingUpdateWeekScore(String kind_rank);

    /**
     *  每月定时更新月路程到总路程。月路程清零
     */
    @Update("update #{kind_rank} set month_distance=0")
    void timingUpdateMonthScore(String kind_rank);

    @Select("Select * from student_rank where student_id =#{student_id}")
    RankInfo selectStuRankInfo(RankInfo rankInfo);
    @Select("Select * from class_rank where student_id =#{student_id}")
    RankInfo selectClaRankInfo(RankInfo rankInfo);

    @Insert("Insert into student_rank set student_id=#{student_id},nickname=#{nickname},day_distance=#{distance},week_distance=#{distance},month_distance=#{distance},all_distance=#{distance},steps=#{steps},college=#{college},duration=#{duration},class_id=#{class_id}")
    Boolean insertStuRankInfoToMysql(RankInfo rankInfo);
    @Insert("Insert into class_rank set class_id=#{class_id},week_distance=#{distance},day_distance=#{distance},month_distance=#{distance},all_distance=#{distance},steps=#{steps},college=#{college},duration=#{duration},class_id=#{class_id}")
    Boolean insertClaRankInfoToMysql(RankInfo rankInfo);


}
