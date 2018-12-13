package team.redrock.running.vo;

import lombok.Data;

@Data
public class UserOtherInfo {

    private String student_id;
    private String class_id;
    private String college;
    private int highest_distance_rank;
    private int highest_distance;
    private int highest_steps_rank;
    private String highest_steps;
    private String total_steps;
    private int total_distance;
    private int highest_continuous_day;

    public UserOtherInfo(User user){
        this.class_id = user.getClass_id();
        this.college = user.getCollege();
        this.student_id = user.getStudent_id();
    }

    public UserOtherInfo(String student_id, String class_id, String college, int highest_distance_rank, int highest_distance, int highest_steps_rank, String highest_steps, String total_steps, int total_distance, int highest_continuous_day) {
        this.student_id = student_id;
        this.class_id = class_id;
        this.college = college;
        this.highest_distance_rank = highest_distance_rank;
        this.highest_distance = highest_distance;
        this.highest_steps_rank = highest_steps_rank;
        this.highest_steps = highest_steps;
        this.total_steps = total_steps;
        this.total_distance = total_distance;
        this.highest_continuous_day = highest_continuous_day;
    }
}
