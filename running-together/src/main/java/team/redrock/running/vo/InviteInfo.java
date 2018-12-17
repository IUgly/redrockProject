package team.redrock.running.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private String[] passive_studentSet;

    public static void main(String[] args) {
        String str = "[\"2016212758\",\"2016212777\"]";
        String ss = str.substring(1,str.length()-1);
        String[] strings = ss.split(",");
        for (String s :strings){
            System.out.println(s.substring(1,s.length()-1));
        }
    }
    public InviteInfo(User invited_user, String passive_Students) {
        this.invited_studentId = invited_user.getStudent_id();
        this.nickname = invited_user.getNickname();
        this.passive_students = passive_Students;
        this.date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        String[] strings = passive_Students.substring
                (1, passive_Students.length()-1).split(",");
        this.passive_studentSet = strings;
        System.out.println(passive_studentSet.toString());
        this.setResult("OK");
    }

    public InviteInfo(String result) {
        this.result = result;
    }
}
