package team.redrock.volunteer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author kuangjunlin
 */
@Configuration
@PropertySource(value = "classpath:define.properties")
public class Config {
    @Value("${volunteer.loginUrl}")
    private String loginUrl;
    @Value("${volunteer.timeUrl}")
    private String timeUrl;
    @Value("${volunteer.rsa}")
    private String rsa;
    @Value("${volunteer.str_priK}")
    private String str_priK;
    @Value("${volunteer.rsa_pubK}")
    private String rsa_pubK;
    @Value("${volunteer.str_pubK}")
    private String str_pubK;

    public String getRsa_pubK() {
        return rsa_pubK;
    }

    public void setRsa_pubK(String rsa_pubK) {
        this.rsa_pubK = rsa_pubK;
    }

    public String getStr_pubK() {
        return str_pubK;
    }

    public void setStr_pubK(String str_pubK) {
        this.str_pubK = str_pubK;
    }

    public String getStr_priK() {
        return str_priK;
    }

    public void setStr_priK(String str_priK) {
        this.str_priK = str_priK;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getTimeUrl() {
        return timeUrl;
    }

    public void setTimeUrl(String timeUrl) {
        this.timeUrl = timeUrl;
    }

    public String getRsa() {
        return rsa;
    }

    public void setRsa(String rsa) {
        this.rsa = rsa;
    }
}
