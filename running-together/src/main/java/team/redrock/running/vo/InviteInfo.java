package team.redrock.running.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Data
public class InviteInfo implements Serializable {
    private String invited_id;
    private String nickname;
    private String invited_studentId;
    private String passive_students;
    private JSONArray student_id;
    private String date;
    private String state;
    private double distance;
    private String result;
    private Set<String> passive_studentSet;

    public InviteInfo(User invited_user, String passive_Students) {
        this.invited_studentId = invited_user.getStudent_id();
        this.nickname = invited_user.getNickname();
        this.passive_students = passive_Students;
        this.date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        String[] strings = passive_Students.split(",");
        for (String s: strings){
            this.passive_studentSet.add(s);
        }
        this.setResult("OK");
    }

    public InviteInfo(String result) {
        this.result = result;
    }
}
