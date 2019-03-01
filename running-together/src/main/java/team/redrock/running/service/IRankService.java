package team.redrock.running.service;

import org.springframework.stereotype.Service;

@Service
public interface IRankService {
    /**
     *  排行榜
     * @param type  类型  （日榜，周榜，月榜，总榜）
     * @param table 查询的表名 （学生路程表， 班级路程表， 学生邀约表）
     * @param page  分页  （默认第一页， 每页10条数据）
     * @return
     */
    String rankList(String type, String table, Integer page);

    /**
     * 名次
     * @param type  类型  （日榜，周榜，月榜，总榜）
     * @param table 查询的表名 （学生路程表， 班级路程表， 学生邀约表）
     * @param student_id    学生学号
     * @return
     */
    String rankPlace(String type, String table, String student_id);
}
