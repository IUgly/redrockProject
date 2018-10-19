package team.redrock.volunteer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource(value = "classpath:define.properties")
public class Config {
    @Value("${volunteer.loginUrl}")
    private String loginUrl;
    @Value("${volunteer.timeUrl}")
    private String timeUrl;
    @Value("${volunteer.rsa}")
    private String rsa;

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
