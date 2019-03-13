package team.redrock.cheerleaders.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import team.redrock.cheerleaders.vo.User;

import java.util.List;

@Mapper
@Component
public interface Dao {
    @Select("SELECT * from user_info WHERE username = #{username} and password = #{password}")
    /**
     * 验证帐号密码是否正确
     *
     */
    User verityAccount(User user);

    @Update("update user_info set username = #{username} where id = 1")
    boolean updateUser(User user);

    @Select("Select student_id,college,nickname From student_distance_rank")
    List<User> users();
}