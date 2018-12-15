package team.redrock.running.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Queue;

/**
 * Created by huangds on 2017/10/28.
 */
@Data
public class User implements Serializable {
    private int id;
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
    private Queue queueInvitations;

    //当前发出的邀约（一个邀约多个其它用户）
    private Map<String, InviteInfo> InvitingMap;
    public void enQueueInvitation(InviteInfo Inviter){
        this.queueInvitations.offer(Inviter);
    }
    public InviteInfo deQueueInvitation(){
        return (InviteInfo) this.queueInvitations.peek();
    }
    public void enInvitingMap(InviteInfo needInviteUser){
        this.getInvitingMap().put(needInviteUser.getInvited_studentId(), needInviteUser);
    }
    public void setInvitedState(InviteInfo passiveUserInvited, String choose){
        this.getInvitingMap().get(passiveUserInvited).setResult(choose);

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
        if (this.nickname==null){
            this.setNickname("有9527名同学和你昵称一样，取个昵称吧！");
        }
    }
    public User(String nickname){
        this.nickname = nickname;
    }
    public User(RankInfo rankInfo){
        this.student_id=rankInfo.getStudent_id();
    }
}
