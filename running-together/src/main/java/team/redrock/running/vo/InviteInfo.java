package team.redrock.running.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class InviteInfo implements Serializable {
    private String invited_id;
    private String nickname;
    private String invited_studentId;
    private JSONArray passive_students;
    private String date;
    private double distance;
    private String state;
    private String[] passive_studentArray;
    //邀请的用户 的回馈结果
    private Map<String, String> result = new HashMap<>();

    public int getSuccessInvitedPersonNum(){
        int num =0;
        for(String key:this.result.keySet()){
            if (this.result.get(key).equals("1")){
                num++;
            }
        }
        return num;
    }

    public InviteInfo(){}

    public InviteInfo(User invited_user, String passive_Students) {
        this.invited_studentId = invited_user.getStudent_id();
        this.nickname = invited_user.getNickname();
        this.date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        String[] strings = passive_Students.substring
                (1, passive_Students.length()-1).split(",");
        this.passive_studentArray = strings;
        for (String s: strings){
            result.put(s,"0");
        }
    }

    public InviteInfo(String result) {
        this.state = result;
    }


}
