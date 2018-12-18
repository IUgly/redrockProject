package team.redrock.running.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import team.redrock.running.dto.InvitationSend;

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

    public InviteInfo(){}

    public InviteInfo(User invited_user, String passive_Students) {
        this.invited_studentId = invited_user.getStudent_id();
        this.nickname = invited_user.getNickname();
        this.passive_students = passive_Students;
        this.date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        String[] strings = passive_Students.substring
                (1, passive_Students.length()-1).split(",");
        this.passive_studentSet = strings;
        this.setResult("OK");
    }

    public InviteInfo(String result) {
        this.result = result;
    }

    public static void main(String[] args) {
        User user = new User();
        user.enQueueInvitation(new InvitationSend("111", "2018","kuangkuang"));
        user.enQueueInvitation(new InvitationSend("222", "2019","xiaotang"));

        InvitationSend invitationSend = user.deQueueInvitation();
        System.out.println(invitationSend.toString());

        InvitationSend invitationSend1 = user.deQueueInvitation();
        System.out.println(invitationSend1.toString());

        InvitationSend invitationSend2 = user.deQueueInvitation();
        if (invitationSend2==null){
            System.out.println("没有了");
        }
    }
}
