package team.redrock.volunteer.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class VolunteerProperties {
    private static String loginUrl;
    private static String timeUrl;
    private static String rsa;

    static {
        Properties prop = new Properties();
        InputStream in = Object.class.getResourceAsStream("/define.properties");
        try {
            prop.load(in);
            loginUrl = prop.getProperty("volunteer.loginUrl").trim();
            timeUrl = prop.getProperty("volunteer.timeUrl").trim();
            rsa = prop.getProperty("volunteer.rsa");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLoginUrl() {
        return loginUrl;
    }

    public static void setLoginUrl(String loginUrl) {
        VolunteerProperties.loginUrl = loginUrl;
    }

    public static String getTimeUrl() {
        return timeUrl;
    }

    public static void setTimeUrl(String timeUrl) {
        VolunteerProperties.timeUrl = timeUrl;
    }

    public static String getRsa() {
        return rsa;
    }

    public static void setRsa(String rsa) {
        VolunteerProperties.rsa = rsa;
    }
}
