package team.redrock.running.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.sql.Date;


@Data
public class Record {
    private String student_id;
    private long begin_time;
    private long end_time;
    private int steps;
    private double distance;
    private Date date;
    private JSONArray lat_lng;
    private int id;
//    private String duration;

    public Record(){
    }

    public Record(String student_id, long begin_time, long end_time, int steps, double distance, Date date, int id) {
        this.student_id = student_id;
        this.begin_time = begin_time;
        this.end_time = end_time;
        this.steps = steps;
        this.distance = distance;
        this.date = date;
        this.id = id;
    }

    public Record(JSONObject json){
        this.student_id = json.getString("student_id");
        this.begin_time = Long.parseLong(json.getString("begin_time"));
        this.end_time = Long.parseLong(json.getString("end_time"));
//        this.duration = String.valueOf(begin_time-end_time);
        this.steps = Integer.parseInt(json.getString("steps"));
        this.distance = Double.valueOf(json.getString("distance"));
        this.date = Date.valueOf(json.getString("date"));
    }
}