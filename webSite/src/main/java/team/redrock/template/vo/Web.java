package team.redrock.template.vo;

public class Web {
    private String name;
    private String url;
    private String type;
    private String kindname;
    private int hot;

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public Web(String name, String url, String type, String kindname, int hot) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.kindname = kindname;
        this.hot = hot;
    }

    public Web(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKindname() {
        return kindname;
    }

    public void setKindname(String kindname) {
        this.kindname = kindname;
    }

    public Web(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
