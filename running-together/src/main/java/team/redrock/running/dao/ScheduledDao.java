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
    @Update("update student_rank s,class_rank c set s.week_distance=0,c.week_distance=0")
    void timingUpdateWeekDistanceScore();

    /**
     *  每月定时更新个人和班级的月路程到总路程。月路程清零
     */
    @Update("update student_id s,class_id c set s.month_distance=0,c.month_distance")
    void timingUpdateMonthDistanceScore();

    /**
     * 查询mysql中有无对应学生或者班级 路程的排名信息，没有则插入  //on DUPLICATE key update
     * @param rankInfo
     * @return
     */
    @Insert("insert into student_distance_rank set student_id=#{student_id},nickname=#{nickname},day_distance=#{distance},week_distance=#{distance},month_distance=#{distance},all_distance=#{distance},college=#{college},duration=#{duration},class_id=#{class_id} on DUPLICATE key update day_distance=#{distance},week_distance = week_distance + #{distance},month_distance=month_distance+#{distance},all_distance=all_distance+#{distance}")
    void updateDayDistanceScoreToStuMysql(RankInfo rankInfo);
    @Insert("insert into class_distance_rank set class_id=#{class_id},day_distance=#{distance},week_distance=#{distance},month_distance=#{distance},all_distance=#{distance},college=#{college},duration=#{duration} on DUPLICATE key update day_distance=#{distance},week_distance = week_distance + #{distance},month_distance=month_distance+#{distance},all_distance=all_distance+#{distance}")
    void updateDayDistanceScoreToClaMysql(RankInfo rankInfo);

    /**
     *  每周定时更新个人和班级的周路程到月路程，总路程。周路程清零
     */
    @Update("update student_rank s,class_rank set s.week_distance=0,c.week_distance=0")
    void timingUpdateWeekInvitedScore();

    /**
     *  每月定时更新个人和班级的月路程到总路程。月路程清零
     */
    @Update("update student_id s,class_id c set s.month_distance=0,c.month_distance")
    void timingUpdateMonthInvitedScore();
    /**
     * 查询mysql中有无对应学生或者班级 邀约的排名信息，没有则插入  //on DUPLICATE key update
     * @param rankInfo
     * @return
     */
    @Insert("insert into student_invitation_rank set student_id=#{student_id},nickname=#{nickname},day_invitation=#{total},week_invitation=#{total},month_invitation=#{total},all_invitation=#{total},college=#{college},class_id=#{class_id} on DUPLICATE key update day_invitation=#{total},week_invitation = week_invitation + #{total},month_invitation=month_invitation+#{total},all_invitation=all_invitation+#{total}")
    void updateDayInviteScoreToStuMysql(RankInfo rankInfo);
}
