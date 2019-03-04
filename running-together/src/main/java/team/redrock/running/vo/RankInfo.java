package team.redrock.running.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RankInfo implements Serializable {
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
        this.class_id = record.getClass_id();
        this.college = record.getCollege();
    }

    public RankInfo(String student_id, String nickname, int total, int rank, String prev_difference) {
        this.student_id = student_id;
        this.nickname = nickname;
        this.total = total;
        this.rank = rank;
        this.prev_difference = prev_difference;
    }

}
