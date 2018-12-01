package team.redrock.running.vo;

import lombok.Data;
import org.springframework.data.redis.core.ZSetOperations;

@Data
public class RankInfo implements ZSetOperations.TypedTuple<String>{
    private String student_id;
    private String class_id;
    private String kind_rank;
    private String nickname;
    private double total;
    private long rank;
    private int steps;
    private String prev_difference;
    private double distance;
    private String college;
    private String duration;

    public RankInfo(){}

    public RankInfo(User user){
        this.nickname=user.getNickname();
        this.student_id=user.getStudent_id();
        this.college = user.getCollege();
        this.class_id =user.getClass_id();
    }
    public RankInfo(Record record){
        this.student_id = record.getStudent_id();
        this.distance = record.getDistance();
    }

    public RankInfo(String student_id, String nickname, int total, int rank, String prev_difference) {
        this.student_id = student_id;
        this.nickname = nickname;
        this.total = total;
        this.rank = rank;
        this.prev_difference = prev_difference;
    }

    public RankInfo(String nickname, int total, int rank, String prev_difference, String class_id) {
        this.nickname = nickname;
        this.total = total;
        this.rank = rank;
        this.prev_difference = prev_difference;
        this.class_id = class_id;
    }

//    @Override
//    public String toString() {
//        return "{" +
//                "\""+"student_id\":" +"\""+ student_id + "\"" +
//                ", \"nickname\":" +"\""+ nickname + "\"" +
//                ",\"college\":"+"\""+ college+ "\""+
//                ", \"class_id\":" + "\""+class_id + "\"" +
//                ",\"duration\":"+"\""+duration+"\""+
//                '}';
//    }

    @Override
    public String toString() {
        return this.student_id;
    }

    public String getInfo(){
        return this.toString();
    }

    @Override
    public String getValue() {
        if (this.nickname==null){
            return this.student_id;
        }
        return this.toString();
    }

    @Override
    public Double getScore() {
        return this.distance;
    }

    @Override
    public int compareTo(ZSetOperations.TypedTuple<String> o) {
        return (int) (this.distance-o.getScore());
    }
}
