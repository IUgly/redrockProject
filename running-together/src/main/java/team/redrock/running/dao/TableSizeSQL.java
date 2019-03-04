package team.redrock.running.dao;

import java.util.Map;

public class TableSizeSQL {
    public String tableSize(Map<String, Object> map){
        String table = (String) map.get("table");
        String student_id = null;
        String sql = null;
        switch (table){
            case "student_distance_rank":
                sql = "SELECT count(*) From student_distance_rank";break;
            case "student_invitation_rank":
                sql = "SELECT count(*) From student_invitation_rank";break;
            case "class_distance_rank":
                sql = "SELECT count(*) From class_distance_rank";break;
            case "invited_record":
                student_id = (String) map.get("student_id");
                sql = "SELECT count(*) From invited_record where invited_student_id = " + student_id;break;
            case "distance_record":
                student_id = (String) map.get("student_id");
                sql = "SELECT count(*) From distance_record where student_id = " + student_id;break;
        }
        return sql;
    }

    public String test(Map<String, Object> map){
        String table = (String) map.get("table");

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT count(*) From ").append(table);

        System.out.println(sql);
        return sql.toString();

    }
}
