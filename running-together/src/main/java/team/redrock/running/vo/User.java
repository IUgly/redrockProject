package team.redrock.running.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

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

    private String idNum;
    private String stuId;

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
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "student_id='" + student_id + '\'' +
//                ", name='" + name + '\'' +
//                ", nickname='" + nickname + '\'' +
//                ", class_id='" + class_id + '\'' +
//                ", token='" + token + '\'' +
//                ", college='" + college + '\'' +
//                ", total=" + total +
//                '}';
//    }

    public User(){}

    public static void main(String[] args) {
        String info = "[{\"total\":3140,\"nickname\":\"无名氏0号\",\"student_id\":\"2017211900\",\"rank\":1},{\"college\":\"计算机科学与技术学院\",\"total\":3020,\"nickname\":\"crown\",\"student_id\":\"2017211903\",\"rank\":2},{\"total\":1520,\"nickname\":\"无名氏2号\",\"student_id\":\"2017211902\",\"rank\":3}]";
//        Gson gson = new Gson();
        JSONArray jsonArray = JSONArray.parseArray(info);
        System.out.println(jsonArray.toJSONString());
    }
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
}
