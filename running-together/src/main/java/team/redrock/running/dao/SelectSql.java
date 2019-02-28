package team.redrock.running.dao;

import java.util.Map;

public class SelectSql {

    public String rankList(Map<String, Object> map){
        String type = (String) map.get("type");
        String table = (String) map.get("table");

        String sql = null;
        Integer page = (Integer) map.get("page");
        Integer pageSize = 15;
        Integer start = (page-1)*pageSize;
        Integer end = page*pageSize;
        if (table.equals("student_distance_rank")){
            switch (type){

                case "days": sql = "SELECT college,day_distance as total,nickname,student_id," +
                        "(SELECT count(DISTINCT day_distance) FROM student_distance_rank AS b " +
                        "WHERE a.day_distance<b.day_distance)+1 AS rank," +
                        "IFNULL((SELECT b.day_distance FROM student_distance_rank AS b\n" +
                        "WHERE b.day_distance>a.day_distance ORDER BY b.day_distance LIMIT 1)-a.day_distance, 0) prev_difference " +
                        "FROM student_distance_rank as a ORDER BY rank " +
                        "LIMIT "+ start +","+ end;break;

                case "weekends":
                    sql = "SELECT college,week_distance as total,nickname,student_id," +
                        "(SELECT count(DISTINCT week_distance) FROM student_distance_rank AS b " +
                        "WHERE a.week_distance<b.week_distance)+1 AS rank," +
                        "IFNULL((SELECT b.week_distance FROM student_distance_rank AS b\n" +
                        "WHERE b.week_distance>a.week_distance ORDER BY b.week_distance LIMIT 1)-a.week_distance, 0) prev_difference " +
                        "FROM student_distance_rank as a ORDER BY rank " +
                        "LIMIT "+ start +","+ end;break;

                case "months" :
                    sql = "SELECT college,month_distance as total,nickname,student_id," +
                        "(SELECT count(DISTINCT month_distance) FROM student_distance_rank AS b " +
                        "WHERE a.month_distance<b.month_distance)+1 AS rank," +
                        "IFNULL((SELECT b.month_distance FROM student_distance_rank AS b\n" +
                        "WHERE b.month_distance>a.month_distance ORDER BY b.month_distance LIMIT 1)-a.month_distance, 0) prev_difference " +
                        "FROM student_distance_rank as a ORDER BY rank " +
                        "LIMIT "+ start +","+ end;break;

                case "all" :
                    sql = "SELECT college,all_distance as total,nickname,student_id," +
                        "(SELECT count(DISTINCT all_distance) FROM student_distance_rank AS b " +
                        "WHERE a.all_distance<b.all_distance)+1 AS rank," +
                        "IFNULL((SELECT b.all_distance FROM student_distance_rank AS b\n" +
                        "WHERE b.all_distance>a.all_distance ORDER BY b.all_distance LIMIT 1)-a.all_distance, 0) prev_difference " +
                        "FROM student_distance_rank as a ORDER BY rank " +
                        "LIMIT "+ start +","+ end;break;
            }
        }
        return sql;
    }
}
