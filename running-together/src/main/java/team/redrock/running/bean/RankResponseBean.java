package team.redrock.running.bean;

import lombok.Data;
import team.redrock.running.enums.UnicomResponseEnums;

@Data
public class RankResponseBean extends ResponseBean{
    private int total;
    private int per_page;

    public RankResponseBean(Object data, UnicomResponseEnums enums,int num) {
        super(data, enums);
        if (num<=15){
            this.total=1;
            this.per_page=num;
        }else {
            this.total=num/15;
            this.per_page=15;
        }
    }
}
