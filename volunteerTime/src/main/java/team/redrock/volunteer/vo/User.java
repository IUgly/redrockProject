package team.redrock.volunteer.vo;

import org.springframework.beans.factory.annotation.Autowired;
import team.redrock.volunteer.config.Config;

import javax.annotation.PostConstruct;

public class User {
    @Autowired
    private Config config;
    private static Config configDouble;

    @PostConstruct
    public void init(){
        configDouble = config;
    }
    private String uid;
    private String account;
    private String password;

    public User(){

    }

    public User(String uid, String account, String password) throws Exception {
        this.uid = uid;
        this.account = account;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
