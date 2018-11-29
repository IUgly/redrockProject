package team.redrock.template.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import team.redrock.template.vo.User;

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
}