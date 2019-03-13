package team.redrock.cheerleaders.vo;

import lombok.Data;

@Data
public class College {
    private String name;
    private Double score;
    private Double rate;
    private Integer voteNum;

    public College(String name) {
        this.name = name;
    }
}
