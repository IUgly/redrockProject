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
    @Update("update rank set week_distance = week_distance + #{distance} where id = #{student_id}")
    void updateScore(RankInfo rankInfo);

    @Select("Select * from rank where student_id =#{student_id}")
    RankInfo selectRankInfo(RankInfo rankInfo);

    @Insert("Insert into rank set student_id=#{student_id},nickname=#{nickname},week_distance=#{distance},month_distance=#{distance},all_distance=#{distance},steps=#{steps},college=#{college},duration=#{duration},class_id=#{class_id}")
    Boolean insertRankInfoToMysql(RankInfo rankInfo);
}
