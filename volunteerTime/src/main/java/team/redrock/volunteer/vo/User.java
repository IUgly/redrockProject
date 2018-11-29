package team.redrock.volunteer.vo;

import team.redrock.volunteer.util.Util;

public class User {
    private String uid;
    private String account;
    private String password;

    public User(){

    }

    public User(String uid, String account, String password) throws Exception {
        this.uid = uid;
        this.account = account;
        this.password = Util.messageDecrypt(password);
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
