package team.redrock.template.vo;

public class Photo {
    private String name;
    private String url;
    private String type;
    private String link;

    public Photo(){

    }
    public Photo(String type) {
        this.type = type;
    }

    public Photo(String name, String url, String type, String link) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.link = link;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
