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
    @Update("update student_rank s,class_rank c set s.day_distance=#{distance},s.week_distance = s.week_distance + #{distance},s.month_distance=s.month_distance+#{distance},s.all_distance=s.all_distance+#{distance},c.day_distance=c.day_distance+#{distance},c.week_distance=c.week_distance+#{distance},c.month_distance=c.month_distance+#{distance},c.all_distance=c.all_distance+#{distance} where s.student_id = #{student_id} AND c.class_id#{class_id}")
    void updateStuScore(RankInfo rankInfo);

    /**
     *  每周定时更新周路程到月路程，总路程。周路程清零
     */
    @Update("update student_rank s,class_rank set s.week_distance=0,c.week_distance=0")
    void timingUpdateWeekScore(String kind_rank);

    /**
     *  每月定时更新月路程到总路程。月路程清零
     */
    @Update("update student_id s,class_id c set s.month_distance=0,c.month_distance")
    void timingUpdateMonthScore(String kind_rank);

    /**
     * 查询mysql中有无对应学生或者班级排名信息，没有则插入
     * @param rankInfo
     * @return
     */
    @Select("Select * from student_rank where student_id =#{student_id}")
    RankInfo selectStuRankInfo(RankInfo rankInfo);
    @Select("Select * from class_rank where student_id =#{student_id}")
    RankInfo selectClaRankInfo(RankInfo rankInfo);

    @Insert("insert into student_rank set student_id=#{student_id},nickname=#{nickname},day_distance=#{distance},week_distance=#{distance},month_distance=#{distance},all_distance=#{distance},steps=#{steps},college=#{college},duration=#{duration},class_id=#{class_id}")
    void insertStuRankInfoToMysql(RankInfo rankInfo);
    @Insert("insert into class_rank set class_id=#{class_id},week_distance=#{distance},day_distance=#{distance},month_distance=#{distance},all_distance=#{distance},steps=#{steps},college=#{college},duration=#{duration},class_id=#{class_id}")
    void insertClaRankInfoToMysql(RankInfo rankInfo);


}
