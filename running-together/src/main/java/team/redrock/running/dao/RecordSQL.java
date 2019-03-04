package team.redrock.running.dao;

import team.redrock.running.vo.RankInfo;

import java.util.Map;

public class RecordSQL {
    public String addRecord(Map map){
        RankInfo rankInfo = (RankInfo) map.get("rankInfo");
        return null;
    }

    public String latLngOrDistance(Map<String, Object> map){
        String student_id = (String) map.get("student_id");
        Integer page = (Integer) map.get("page");
        String type = (String) map.get("type");

        Integer pageSize = 10;
        Integer start = (page-1)*pageSize;
        Integer end = page*pageSize;

        String sql = null;
        if (type.equals("invited_record")){
            sql = "select distance,date,passive_students,invited_id " +
                    "from invited_record " +
                    "where invited_student_id="+ student_id +
                    " LIMIT "+start + ", " + end;
        }else if (type.equals("distance_record")){
            sql = "select * from distance_record where student_id = " +student_id +
                    " LIMIT "+ start + ", " + end;
        }

        return sql;
    }

}
