package team.redrock.running.bean;

import lombok.Data;
import team.redrock.running.enums.UnicomResponseEnums;

@Data
public class RankResponseBean extends ResponseBean{
    private int totalPage;
    private int this_pageSize;

    public RankResponseBean(Object data, UnicomResponseEnums enums,int num, Integer thisPageSize) {
        super(data, enums);
        if (num <= 10){
            this.totalPage=1;
            this.this_pageSize=num;
        }else {
            this.totalPage=num/10+1;
            this.this_pageSize=thisPageSize;
        }
    }

    public RankResponseBean(Object data, UnicomResponseEnums enums,int num) {
        super(data, enums);
        if (num <= 10){
            this.totalPage=1;
            this.this_pageSize=num;
        }else {
            this.totalPage=num/10+1;
            this.this_pageSize=10;
        }
    }
}
