package team.redrock.cheerleaders.vo;

/**
 * Created by huangds on 2017/10/28.
 */

public class User {

    private long id;

    private String userid;

    private String username;

    private String password;

    private String userps;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserps() {
        return userps;
    }

    public void setUserps(String userps) {
        this.userps = userps;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
