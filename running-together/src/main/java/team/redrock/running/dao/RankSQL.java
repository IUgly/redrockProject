package team.redrock.running.dao;

import java.util.Map;

public class RankSQL {
    public String rankList(Map<String, Object> map){
        String type = (String) map.get("type");
        if (type.equals("all")){
            type = "alls";
        }
        String table = (String) map.get("table");
        Integer page = (Integer) map.get("page");

        Integer pageSize = 10;
        Integer start = (page-1)*pageSize;
        Integer end = page*pageSize;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT college,class_id,").append(type).
                append(" as total,");

        if (table.equals("student_distance_rank")||
            table.equals("student_invitation_rank")){

            sql.append("nickname,student_id,");
        }
        sql.append("(SELECT count(DISTINCT ")
           .append(type).append(") FROM ").append(table).append(" AS b ")
           .append("WHERE a.").append(type).append("<b.").append(type).append(")+1 AS rank ")
           .append("FROM ").append(table).append(" as a ORDER BY rank ")
           .append("LIMIT ").append(start).append(",").append(end);


        return sql.toString();
    }

    public String rankPlace(Map<String, Object> map){
        String type = (String) map.get("type");
        if (type.equals("all")){
            type = "alls";
        }
        String table = (String) map.get("table");
        String id = (String) map.get("id");
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT college,");
        sql.append(type);
        sql.append(" as total,");
        if (table.equals("student_distance_rank")||
                table.equals("student_invitation_rank")){
            sql.append("nickname, student_id,");
        }else if (table.equals("class_distance_rank")){
            sql.append("class_id,");
        }
        sql.append("(SELECT count(DISTINCT ").append(type).append(") FROM ").append(table).append(" AS b \n")
           .append("WHERE a.").append(type).append("<b.").append(type).append(")+1 AS rank,\n")
           .append("IFNULL((SELECT b.").append(type).append(" FROM ").append(table).append(" AS b\n")
           .append("WHERE b.").append(type).append(">a.").append(type)
           .append(" ORDER BY b.").append(type).append(" LIMIT 1)-a.").append(type).append(",0) ")
           .append("prev_difference \n")
           .append("FROM ").append(table).append(" AS a WHERE a.");
        if (table.equals("student_distance_rank")||
            table.equals("student_invitation_rank")){
            sql.append("student_id=").append(id);
        }else if (table.equals("class_distance_rank")){
            sql.append("class_id=").append(id);
        }


        return sql.toString();
    }
}
