package team.redrock.running.vo;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class InviteInfo {
    private String invited_id;
    private String nickname;
    private String invited_studentId;
    private String passive_students;
    private long date;
    private String state;
    private double distance;
    private String result;
    private Set<String> passive_studentSet;

    public InviteInfo(User invited_user, String passive_Students) {
        this.invited_studentId = invited_user.getStudent_id();
        this.nickname = invited_user.getNickname();
        this.passive_students = passive_Students;
        this.date = new Date().getTime();
        String[] strings = passive_Students.split(",");
        for (String s: strings){
            this.passive_studentSet.add(s);
        }
    }
}
