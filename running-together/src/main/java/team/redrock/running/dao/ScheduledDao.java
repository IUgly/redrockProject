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
     *  从redis更新到mysql
     * @param rankInfo 当日某用户排名信息
     */
    @Update("update rank set day_distance=#{distance},week_distance = week_distance + #{distance},month_distance=month_distance+#{distance},all_distance=all_distance+#{distance} where student_id = #{student_id}")
    void updateScore(RankInfo rankInfo);

    /**
     *  每周定时更新周路程到月路程，总路程。周路程清零
     */
    @Update("update rank set month_distance=month_distance+ week_distance,all_distance=all_distance+week_distance,week_distance=0")
    void updateWeekScore();

    /**
     *  每月定时更新月路程到总路程。月路程清零
     */
    @Update("update rank set all_distance=all_distance+month_distance,month_distance=0")
    void updateMonthScore();

    @Select("Select * from rank where student_id =#{student_id}")
    RankInfo selectRankInfo(RankInfo rankInfo);

    @Insert("Insert into rank set student_id=#{student_id},nickname=#{nickname},week_distance=#{distance},month_distance=#{distance},all_distance=#{distance},steps=#{steps},college=#{college},duration=#{duration},class_id=#{class_id}")
    Boolean insertRankInfoToMysql(RankInfo rankInfo);
}
