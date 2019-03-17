package team.redrock.cheerleaders.vo;

import lombok.Data;

@Data
public class College {
    private String name;
    private Double score;
    private Double rate;
    private Double voteNum;
    private Integer integer;

    public College(String name) {
        this.name = name;
    }

    public College(String name, Double score, Double voteNum) {
        this.name = name;
        this.score = score;
        this.voteNum = voteNum;
    }
}
