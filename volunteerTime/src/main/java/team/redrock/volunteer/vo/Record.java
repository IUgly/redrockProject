package team.redrock.volunteer.vo;

public class Record {
    private String hours;
    private String content;
    private String start_time;
    private String title;
    private String addWay;
    private String status;
    private String server_group;
    private String uid;

    public void JsonResponse(String hours, String content, String start_time, String title, String addWay, String status, String server_group){
        this.addWay=addWay;
        this.title=title;
        this.content=content;
        this.hours=hours;
        this.server_group=server_group;
        this.start_time=start_time;
        this.status=status;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddWay() {
        return addWay;
    }

    public void setAddWay(String addWay) {
        this.addWay = addWay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServer_group() {
        return server_group;
    }

    public void setServer_group(String server_group) {
        this.server_group = server_group;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
