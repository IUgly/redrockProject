package team.redrock.running.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import team.redrock.running.dto.InvitationSend;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by huangds on 2017/10/28.
 */
@Data
public class User implements Serializable {
    private String student_id;
    private String name;
    private String nickname;
    private String class_id;
    private String token;
    private String college;
    private String password;
    private double total;
    private String state;
    private String idNum;
    private String stuId;
    //当前收到的邀约(可能多个邀约)
    private Deque queueInvitations = new ArrayDeque();

    //当前发出的邀约（一个邀约多个其它用户）
    private String invitingNow;

    public void enQueueInvitation(InvitationSend invitationSend){
        this.queueInvitations.offerLast(invitationSend);
    }
    public InvitationSend deQueueInvitation(){
        return (InvitationSend) this.queueInvitations.pollFirst();
    }


    public User(String student_id, String name, String nickname, String class_id, String token, String college) {
        this.student_id = student_id;
        this.name = name;
        this.nickname = nickname;
        this.class_id = class_id;
        this.token = token;
        this.college = college;
    }
    @Override
    public String toString() {
        return "{" +
                "\"student_id\":" + "\""+student_id +"\""+
                ",\"nickname\":" + "\""+nickname  +"\""+
//                ",\"total\":" + total +
                ",\"college\":" + "\""+college  +"\""+
//                ",\"rank\":" + rank +
                '}';
    }


    public User(){}

    public User(JSONObject json){
        this.class_id = json.get("classNum").toString();
        this.college = json.get("college").toString();
        this.name = json.get("name").toString();
        this.student_id = json.get("stuNum").toString();
    }
    public User(String nickname){
        this.nickname = nickname;
    }
    public User(RankInfo rankInfo){
        this.student_id=rankInfo.getStudent_id();
    }

    public User(User user) {
        this.class_id = user.getClass_id();
        this.college = user.getCollege();
        this.student_id = user.getStudent_id();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.queueInvitations = user.queueInvitations;
    }
}
