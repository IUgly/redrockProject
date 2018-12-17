package team.redrock.running.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClassRank implements Serializable {
    private String class_id;

    private String college;

    private double total;
    private String duration;
    private int rank;
}
