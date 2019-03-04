package team.redrock.running.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;
import team.redrock.running.vo.RankInfo;

import java.util.List;

@Mapper
@Component
public interface RankDao {

    @SelectProvider(type = RankSQL.class, method = "rankList")
    List<RankInfo> rankList(@Param("type") String type,
                            @Param("table") String table,
                            @Param("page") Integer page);

    @SelectProvider(type = TableSizeSQL.class, method = "tableSize")
    Integer rankNum(@Param("table") String table);

    @SelectProvider(type = RankSQL.class, method = "rankPlace")
    RankInfo rankPlace (@Param("type") String type, @Param("table") String table,
                        @Param("id") String id);

}
