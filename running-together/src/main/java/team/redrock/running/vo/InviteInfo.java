package team.redrock.running.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
    private Integer acceptUsersNum;
    //邀请的用户 的回馈结果
    private Map<String, String> result = new HashMap<>();
    private String resultString;

    public String getResultString() {
        JSONArray jsonArray = new JSONArray();

        result.forEach((s, r) -> {
            JSONObject json = new JSONObject();
            json.put("student_id", s);
            json.put("result", r);

            jsonArray.add(json);
        });

        return jsonArray.toJSONString();
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
            result.put(s, "0");
        }
    }

    public InviteInfo(String invited_id, String nickname) {
        this.invited_id = invited_id;
        this.nickname = nickname;
    }

    public InviteInfo(String result) {
        this.state = result;
    }


}
