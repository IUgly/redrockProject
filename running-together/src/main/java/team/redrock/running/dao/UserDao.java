package team.redrock.running.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.User;
import team.redrock.running.vo.UserOtherInfo;
import team.redrock.running.vo.Vo;

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

    @Select("select student_id,nickname from user where student_id = #{student_id}")
    User selectSimpleUserInfo(String student_id);

    @Select("select * from user_other_info where student_id = #{student_id}")
    UserOtherInfo getUserOtherInfo(String student_id);

    @Select("select * from user where name = #{name}")
    List<User> getUserListByName(String name);

    @Select("SELECT college,#{type} as total,nickname,student_id," +
            "(SELECT count(DISTINCT #{type}) FROM student_distance_rank AS b WHERE a.#{type}<b.#{type})+1 AS rank," +
            "IFNULL((SELECT b.#{type} FROM student_distance_rank AS b WHERE b.#{type}>a.#{type}" +
            " ORDER BY b.#{type} LIMIT 1)-a.#{type}, 0) prev_difference " +
            "FROM student_distance_rank as a ORDER BY rank LIMIT #{start},#{end}")
    List<RankInfo> rank(Vo vo);

    @SelectProvider(type = SelectSql.class, method = "rankList")
    List<RankInfo> rankList(@Param("type") String type, @Param("table") String table,
                            @Param("page") Integer page);
//    @SelectProvider(type = SelectSql.class,method = "searchDogsCount")//通过类来反射sql语句
//        Integer getYeSeTotaily(@Param("color") String color,@Param("type") String type,
//                               @Param("shape") String shape,@Param("sex") String sex,@Param("flag") Integer flag);
    //

}