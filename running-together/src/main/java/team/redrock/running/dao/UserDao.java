package team.redrock.running.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.User;
import team.redrock.running.vo.UserOtherInfo;

@Mapper
@Component
public interface UserDao {

    @Insert("Insert into user set student_id=#{student_id},name=#{name},nickname=#{class_id},class_id=#{class_id},college=#{college}")
    Boolean insertUser(User user);

    @Insert("Insert into user_other_info set student_id=#{student_id},class_id=#{class_id},college=#{college},highest_distance_rank=#{highest_distance_rank},highest_distance=#{highest_distance},highest_steps_rank=#{highest_steps_rank},highest_steps=#{highest_steps},total_steps={total_steps},total_distance=#{total_distance},highest_continuous_day=#{highest_continuous_day}")
    Boolean insertUserOtherInfo(UserOtherInfo userOtherInfo);

    @Update("update user set nickname = #{nickname} where student_id=#{student_id}")
    Boolean updateUserInfo(User user);

    @Select("select * from user where student_id = #{student_id}")
    User selectUserByStudentId(String student_id);

    @Select("select student_id,nickname from user where student_id = #{student_id}")
    User selectSimpleUserInfo(String student_id);
}