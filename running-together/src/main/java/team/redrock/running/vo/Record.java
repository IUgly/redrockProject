package team.redrock.running.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;


@Data
public class Record implements Serializable {
    private String student_id;
    private String class_id;
    private long begin_time;
    private long end_time;
    private int steps;
    private double distance;
    private Date date;
    private JSONArray lat_lng;
    private String id;
    private String invited_id;
    private String college;
//    private String duration;


    public Record(JSONObject json,String invited_id) {
        this.student_id = json.getString("student_id");
        this.begin_time = Long.parseLong(json.getString("begin_time"));
        this.end_time = Long.parseLong(json.getString("end_time"));
        this.steps = Integer.parseInt(json.getString("steps"));
        this.distance = Double.valueOf(json.getString("distance"));
        this.date = Date.valueOf(json.getString("date"));
        this.setLat_lng(json.getJSONArray("lat_lng"));
        this.invited_id = invited_id;
    }

    public Record(JSONObject json, String invited_id, String student_id) {
        this.begin_time = Long.parseLong(json.getString("begin_time"));
        this.end_time = Long.parseLong(json.getString("end_time"));
        this.steps = Integer.parseInt(json.getString("steps"));
        this.distance = Double.valueOf(json.getString("distance"));
        this.date = Date.valueOf(json.getString("date"));
        this.setLat_lng(json.getJSONArray("lat_lng"));
        this.invited_id = invited_id;
        this.student_id = student_id;
    }

    public Record(){
    }


    public Record(JSONObject json){
        this.student_id = json.getString("student_id");
        this.begin_time = Long.parseLong(json.getString("begin_time"));
        this.end_time = Long.parseLong(json.getString("end_time"));
        this.steps = Integer.parseInt(json.getString("steps"));
        this.distance = Double.valueOf(json.getString("distance"));
        this.date = Date.valueOf(json.getString("date"));
        this.setLat_lng(json.getJSONArray("lat_lng"));
    }

}
