package team.redrock.running.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Data
public class InviteInfo implements Serializable {
    private String invited_id;
    private String nickname;
    private String invited_studentId;
    private String passive_students;
    private JSONArray student_id;
    private String date;
    private double distance;
    private String state;
    private String[] passive_studentSet;
    //邀请的用户 的回馈结果
    private Map<String, String> result;

    public InviteInfo(){}

    public InviteInfo(User invited_user, String passive_Students) {
        this.invited_studentId = invited_user.getStudent_id();
        this.nickname = invited_user.getNickname();
        this.passive_students = passive_Students;
        this.date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        String[] strings = passive_Students.substring
                (1, passive_Students.length()-1).split(",");
        this.passive_studentSet = strings;
        this.setDate("OK");
        for (String s: strings){
            result.put(s,"0");
        }
    }

    public InviteInfo(String result) {
        this.state = result;
    }


}
