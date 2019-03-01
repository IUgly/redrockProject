package team.redrock.running.dao;

import java.util.Map;

public class RankSQL {

    public String rankList(Map<String, Object> map){
        String type = (String) map.get("type");
        String table = (String) map.get("table");
        Integer page = (Integer) map.get("page");

        String sql = null;
        Integer pageSize = 10;
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
        }else if (table.equals("class_distance_rank")){
            switch (type){

                case "days": sql = "SELECT college,day_distance as total,class_id," +
                        "(SELECT count(DISTINCT day_distance) FROM class_distance_rank AS b " +
                        "WHERE a.day_distance<b.day_distance)+1 AS rank," +
                        "IFNULL((SELECT b.day_distance FROM class_distance_rank AS b\n" +
                        "WHERE b.day_distance>a.day_distance ORDER BY b.day_distance LIMIT 1)-a.day_distance, 0) prev_difference " +
                        "FROM class_distance_rank as a ORDER BY rank " +
                        "LIMIT "+ start +","+ end;break;

                case "weekends":
                    sql = "SELECT college,week_distance as total,class_id," +
                            "(SELECT count(DISTINCT week_distance) FROM class_distance_rank AS b " +
                            "WHERE a.week_distance<b.week_distance)+1 AS rank," +
                            "IFNULL((SELECT b.week_distance FROM student_distance_rank AS b\n" +
                            "WHERE b.week_distance>a.week_distance ORDER BY b.week_distance LIMIT 1)-a.week_distance, 0) prev_difference " +
                            "FROM class_distance_rank as a ORDER BY rank " +
                            "LIMIT "+ start +","+ end;break;

                case "months" :
                    sql = "SELECT college,month_distance as total,class_id," +
                            "(SELECT count(DISTINCT month_distance) FROM class_distance_rank AS b " +
                            "WHERE a.month_distance<b.month_distance)+1 AS rank," +
                            "IFNULL((SELECT b.month_distance FROM class_distance_rank AS b\n" +
                            "WHERE b.month_distance>a.month_distance ORDER BY b.month_distance LIMIT 1)-a.month_distance, 0) prev_difference " +
                            "FROM class_distance_rank as a ORDER BY rank " +
                            "LIMIT "+ start +","+ end;break;

                case "all" :
                    sql = "SELECT college,all_distance as total,class_id," +
                            "(SELECT count(DISTINCT all_distance) FROM class_distance_rank AS b " +
                            "WHERE a.all_distance<b.all_distance)+1 AS rank," +
                            "IFNULL((SELECT b.all_distance FROM class_distance_rank AS b\n" +
                            "WHERE b.all_distance>a.all_distance ORDER BY b.all_distance LIMIT 1)-a.all_distance, 0) prev_difference " +
                            "FROM class_distance_rank as a ORDER BY rank " +
                            "LIMIT "+ start +","+ end;break;
            }
        }else if (table.equals("student_invitation_rank")){
            switch (type){

                case "days":
                    sql = "SELECT college,day_invitation as total,nickname,student_id,\n" +
                        "(SELECT count(DISTINCT day_invitation) FROM student_invitation_rank AS b " +
                        "WHERE a.day_invitation<b.day_invitation)+1 AS rank,\n" +
                        "IFNULL((SELECT b.day_invitation FROM student_invitation_rank AS b \n" +
                        "WHERE b.day_invitation>a.day_invitation ORDER BY b.day_invitation LIMIT 1)-a.day_invitation, 0) prev_difference \n" +
                        "FROM student_invitation_rank as a ORDER BY rank" +
                        " LIMIT "+ start +","+ end;break;

                case "weekends":
                    sql = "SELECT college,week_invitation as total,nickname,student_id,\n" +
                            "(SELECT count(DISTINCT week_invitation) FROM student_invitation_rank AS b " +
                            "WHERE a.week_invitation<b.week_invitation)+1 AS rank,\n" +
                            "IFNULL((SELECT b.week_invitation FROM student_invitation_rank AS b \n" +
                            "WHERE b.week_invitation>a.week_invitation ORDER BY b.week_invitation LIMIT 1)-a.week_invitation, 0) prev_difference \n" +
                            "FROM student_invitation_rank as a ORDER BY rank" +
                            " LIMIT "+ start +","+ end;break;

                case "months" :
                    sql = "SELECT college,month_invitation as total,nickname,student_id,\n" +
                            "(SELECT count(DISTINCT month_invitation) FROM student_invitation_rank AS b " +
                            "WHERE a.month_invitation<b.month_invitation)+1 AS rank,\n" +
                            "IFNULL((SELECT b.month_invitation FROM student_invitation_rank AS b \n" +
                            "WHERE b.month_invitation>a.month_invitation ORDER BY b.month_invitation LIMIT 1)-a.month_invitation, 0) prev_difference \n" +
                            "FROM student_invitation_rank as a ORDER BY rank" +
                            " LIMIT "+ start +","+ end;break;

                case "all" :
                    sql = "SELECT college,all_invitation as total,nickname,student_id,\n" +
                            "(SELECT count(DISTINCT all_invitation) FROM student_invitation_rank AS b " +
                            "WHERE a.all_invitation<b.all_invitation)+1 AS rank,\n" +
                            "IFNULL((SELECT b.all_invitation FROM student_invitation_rank AS b \n" +
                            "WHERE b.all_invitation>a.all_invitation ORDER BY b.all_invitation LIMIT 1)-a.all_invitation, 0) prev_difference \n" +
                            "FROM student_invitation_rank as a ORDER BY rank" +
                            " LIMIT "+ start +","+ end;break;
            }
        }
        return sql;
    }

    public String rankNum(Map<String, Object> map){
        String table = (String) map.get("table");
        String sql = null;
        switch (table){
            case "student_distance_rank":
                sql = "SELECT count(*) From student_distance_rank";break;
            case "student_invitation_rank":
                sql = "SELECT count(*) From student_invitation_rank";break;
            case "class_distance_rank":
                sql = "SELECT count(*) From class_distance_rank";break;
        }
        return sql;
    }

    public String rankPlace(Map<String, Object> map){
        String type = (String) map.get("type");
        String table = (String) map.get("table");
        String id = (String) map.get("id");

        String sql = null;
        if (table.equals("student_distance_rank")){
            switch (type){

                case "days":
                    sql = "SELECT college,day_distance as total, nickname, student_id,class_id,\n" +
                            "(SELECT count(DISTINCT day_distance) FROM student_distance_rank AS b \n" +
                            "WHERE a.day_distance<b.day_distance)+1 AS rank,\n" +
                            "IFNULL((SELECT b.day_distance FROM student_distance_rank AS b\n" +
                            "WHERE b.day_distance>a.day_distance " +
                            "ORDER BY b.day_distance LIMIT 1)-a.day_distance,0) " +
                            "prev_difference \n" +
                            "FROM student_distance_rank AS a WHERE a.student_id = "+ id;break;

                case "weekends":
                    sql = "SELECT college,week_distance as total, nickname, student_id,class_id,\n" +
                            "(SELECT count(DISTINCT day_distance) FROM student_distance_rank AS b \n" +
                            "WHERE a.week_distance<b.week_distance)+1 AS rank,\n" +
                            "IFNULL((SELECT b.week_distance FROM student_distance_rank AS b\n" +
                            "WHERE b.week_distance>a.week_distance " +
                            "ORDER BY b.week_distance LIMIT 1)-a.week_distance,0) " +
                            "prev_difference \n" +
                            "FROM student_distance_rank AS a WHERE a.student_id = "+ id;break;

                case "months" :
                    sql = "SELECT college,month_distance as total, nickname, student_id,class_id,\n" +
                            "(SELECT count(DISTINCT month_distance) FROM student_distance_rank AS b \n" +
                            "WHERE a.month_distance<b.month_distance)+1 AS rank,\n" +
                            "IFNULL((SELECT b.month_distance FROM student_distance_rank AS b\n" +
                            "WHERE b.month_distance>a.month_distance " +
                            "ORDER BY b.month_distance LIMIT 1)-a.month_distance,0) " +
                            "prev_difference \n" +
                            "FROM student_distance_rank AS a WHERE a.student_id = "+ id;break;

                case "all" :
                    sql = "SELECT college,all_distance as total, nickname, student_id,class_id,\n" +
                            "(SELECT count(DISTINCT all_distance) FROM student_distance_rank AS b \n" +
                            "WHERE a.all_distance<b.all_distance)+1 AS rank,\n" +
                            "IFNULL((SELECT b.all_distance FROM student_distance_rank AS b\n" +
                            "WHERE b.all_distance>a.all_distance " +
                            "ORDER BY b.all_distance LIMIT 1)-a.all_distance,0) " +
                            "prev_difference \n" +
                            "FROM student_distance_rank AS a WHERE a.student_id = "+ id;break;
            }
        }else if (table.equals("class_distance_rank")){
            switch (type){

                case "days":
                    sql = "SELECT college,day_distance as total,class_id,\n" +
                            "(SELECT count(DISTINCT day_distance) " +
                            "FROM class_distance_rank AS b \n" +
                            "WHERE a.day_distance<b.day_distance)+1 AS rank,\n" +
                            "IFNULL((SELECT b.day_distance FROM class_distance_rank AS b\n" +
                            "WHERE b.day_distance>a.day_distance " +
                            "ORDER BY b.day_distance LIMIT 1)-a.day_distance,0) " +
                            "prev_difference \n" +
                            "FROM class_distance_rank AS a WHERE a.class_id = "+ id;break;

                case "weekends":
                    sql = "SELECT college,week_distance as total,class_id,\n" +
                            "(SELECT count(DISTINCT week_distance) " +
                            "FROM class_distance_rank AS b \n" +
                            "WHERE a.week_distance<b.week_distance)+1 AS rank,\n" +
                            "IFNULL((SELECT b.week_distance FROM class_distance_rank AS b\n" +
                            "WHERE b.week_distance>a.week_distance " +
                            "ORDER BY b.week_distance LIMIT 1)-a.week_distance, 0) " +
                            "prev_difference \n" +
                            "FROM class_distance_rank AS a WHERE a.class_id = "+ id;break;

                case "months" :
                    sql = "SELECT college,month_distance as total,class_id,\n" +
                            "(SELECT count(DISTINCT month_distance) " +
                            "FROM class_distance_rank AS b \n" +
                            "WHERE a.month_distance<b.month_distance)+1 AS rank,\n" +
                            "IFNULL((SELECT b.month_distance FROM class_distance_rank AS b\n" +
                            "WHERE b.month_distance>a.month_distance " +
                            "ORDER BY b.month_distance LIMIT 1)-a.month_distance,0) " +
                            "prev_difference \n" +
                            "FROM class_distance_rank AS a WHERE a.class_id = "+ id;break;

                case "all" :
                    sql = "SELECT college,all_distance as total,class_id,\n" +
                            "(SELECT count(DISTINCT all_distance) " +
                            "FROM class_distance_rank AS b \n" +
                            "WHERE a.all_distance<b.all_distance)+1 AS rank,\n" +
                            "IFNULL((SELECT b.all_distance FROM class_distance_rank AS b\n" +
                            "WHERE b.all_distance>a.all_distance " +
                            "ORDER BY b.all_distance LIMIT 1)-a.all_distance,0) " +
                            "AS prev_difference \n" +
                            "FROM class_distance_rank AS a WHERE a.class_id = "+ id;break;
            }
        }else if (table.equals("student_invitation_rank")){
            switch (type){

                case "days":
                    sql = "SELECT college,day_invitation as total,student_id,class_id,\n" +
                            "(SELECT count(DISTINCT day_invitation) " +
                            "FROM student_invitation_rank AS b \n" +
                            "WHERE a.day_invitation<b.day_invitation)+1 AS rank,\n" +
                            "IFNULL(SELECT b.day_invitation FROM student_invitation_rank AS b\n" +
                            "WHERE b.day_invitation>a.day_invitation " +
                            "ORDER BY b.day_invitation LIMIT 1)-a.day_invitation,0) " +
                            "prev_difference \n" +
                            "FROM student_invitation_rank AS a WHERE a.student_id = "+ id;break;

                case "weekends":
                    sql = "SELECT college,week_invitation as total,student_id,class_id,\n" +
                            "(SELECT count(DISTINCT week_invitation) " +
                            "FROM student_invitation_rank AS b \n" +
                            "WHERE a.week_invitation<b.week_invitation)+1 AS rank,\n" +
                            "IFNULL((SELECT b.week_invitation FROM student_invitation_rank AS b\n" +
                            "WHERE b.week_invitation>a.week_invitation " +
                            "ORDER BY b.week_invitation LIMIT 1)-a.week_invitation,0) " +
                            "prev_difference \n" +
                            "FROM student_invitation_rank AS a WHERE a.student_id = "+ id;break;

                case "months" :
                    sql = "SELECT college,month_invitation as total,student_id,class_id,\n" +
                            "(SELECT count(DISTINCT day_invitation) " +
                            "FROM student_invitation_rank AS b \n" +
                            "WHERE a.month_invitation<b.month_invitation)+1 AS rank,\n" +
                            "IFNULL((SELECT b.month_invitation FROM student_invitation_rank AS b\n" +
                            "WHERE b.month_invitation>a.month_invitation " +
                            "ORDER BY b.month_invitation LIMIT 1)-a.month_invitation,0) " +
                            "prev_difference \n" +
                            "FROM student_invitation_rank AS a WHERE a.student_id = "+ id;break;

                case "all" :
                    sql = "SELECT college,all_invitation as total,student_id,class_id,\n" +
                            "(SELECT count(DISTINCT day_invitation) " +
                            "FROM student_invitation_rank AS b \n" +
                            "WHERE a.all_invitation<b.all_invitation)+1 AS rank,\n" +
                            "IFNULL((SELECT b.all_invitation FROM student_invitation_rank AS b\n" +
                            "WHERE b.all_invitation>a.all_invitation " +
                            "ORDER BY b.all_invitation LIMIT 1)-a.all_invitation,0) " +
                            "prev_difference \n" +
                            "FROM student_invitation_rank AS a WHERE a.student_id = "+ id;break;
            }
        }

        return sql;
    }

}
