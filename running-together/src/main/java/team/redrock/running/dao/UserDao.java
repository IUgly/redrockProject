package team.redrock.running.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.User;
import team.redrock.running.vo.UserOtherInfo;

import java.util.List;

@Mapper
@Component
public interface UserDao {

    /**
     *  replace语句：如果mysql中存在与插入数据的键值相同的数据，则更新数据，如果没有相同则插入
     * @param user
     * @return
     */
    @Insert("replace into user set student_id=#{student_id},name=#{name},nickname=#{nickname},class_id=#{class_id},college=#{college}")
    Boolean insertUser(User user);

    @Insert("replace into user_other_info set student_id=#{student_id},class_id=#{class_id},college=#{college},highest_distance_rank=#{highest_distance_rank},highest_distance=#{highest_distance},highest_steps_rank=#{highest_steps_rank},highest_steps=#{highest_steps},total_steps={total_steps},total_distance=#{total_distance},highest_continuous_day=#{highest_continuous_day}")
    Boolean insertUserOtherInfo(UserOtherInfo userOtherInfo);

    @Update("update user set nickname = #{nickname} where student_id=#{student_id}")
    Boolean updateUserInfo(User user);

    @Select("select * from user where student_id = #{student_id}")
    User selectUserByStudentId(String student_id);

    @ResultType(team.redrock.running.vo.User.class)
    @Select("select nickname,student_id from user where student_id = #{student_id}")
    User selectSimpleUserInfo(String student_id);

    @Select("select * from user_other_info where student_id = #{student_id}")
    UserOtherInfo getUserOtherInfo(String student_id);

    @Select("select * from user where name = #{name}")
    List<User> getUserListByName(String name);

}