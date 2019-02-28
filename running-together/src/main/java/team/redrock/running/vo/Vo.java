package team.redrock.running.vo;

public class Vo {
    private String type;
    private String table;
    private Integer start;
    private Integer end;

    public Vo(String type, String table, Integer start, Integer end) {
        this.type = type;
        this.table = table;
        this.start = start;
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }
}
