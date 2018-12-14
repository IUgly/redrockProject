package team.redrock.running.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.InviteInfo;

@Mapper
@Component
public interface InvitedDao {
    @Insert("Insert into invited_record set invited_student_id = #{invited_studentId}, passive_students=#{passive_students}, date=#{date}")
    @Options(useGeneratedKeys=true, keyProperty="invited_id", keyColumn="invited_id")
    void insertInvited(InviteInfo inviteInfo);
}
