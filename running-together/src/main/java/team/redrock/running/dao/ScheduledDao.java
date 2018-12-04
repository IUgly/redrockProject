package team.redrock.running.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.RankInfo;

@Mapper
@Component
public interface ScheduledDao {
    /**
     *  每周定时更新个人和班级的周路程到月路程，总路程。周路程清零
     */
    @Update("update student_rank s,class_rank set s.week_distance=0,c.week_distance=0")
    void timingUpdateWeekScore();

    /**
     *  每月定时更新个人和班级的月路程到总路程。月路程清零
     */
    @Update("update student_id s,class_id c set s.month_distance=0,c.month_distance")
    void timingUpdateMonthScore();

    /**
     * 查询mysql中有无对应学生或者班级排名信息，没有则插入  //on DUPLICATE key update
     * @param rankInfo
     * @return
     */
    @Insert("insert into student_rank set student_id=#{student_id},nickname=#{nickname},day_distance=#{distance},week_distance=#{distance},month_distance=#{distance},all_distance=#{distance},steps=#{steps},college=#{college},duration=#{duration},class_id=#{class_id} on DUPLICATE key update day_distance=#{day_distance},week_distance = week_distance + #{distance},month_distance=month_distance+#{distance},all_distance=all_distance+#{distance}")
    void updateDayScoreToStuMysql(RankInfo rankInfo);
    @Insert("insert into class_rank set class_id=#{class_id},day_distance=#{distance},week_distance=#{distance},month_distance=#{distance},all_distance=#{distance},steps=#{steps},college=#{college},duration=#{duration} on DUPLICATE key update day_distance=#{day_distance},week_distance = week_distance + #{distance},month_distance=month_distance+#{distance},all_distance=all_distance+#{distance}")
    void updateDayScoreToClaMysql(RankInfo rankInfo);
}
