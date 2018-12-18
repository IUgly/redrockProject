package team.redrock.running.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.InviteInfo;

@Mapper
@Component
public interface InvitedDao {
    @Select("select * from invited_record where invited_id = #{invited_id}")
    InviteInfo selectInvitedById(String invited_id);
}
