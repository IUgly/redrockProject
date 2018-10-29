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
