package team.redrock.volunteer.util;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.redrock.volunteer.config.Config;

import javax.annotation.PostConstruct;

@Component
public class ImitateLogin {
    @Autowired
    private Config config;
    private static Config configDouble;

    @PostConstruct
    public void init(){
        configDouble = config;
    }

    public static String ImitateLogin(String name, String pswd) {
        String loginUrl = configDouble.getLoginUrl();
        String dataUrl = configDouble.getTimeUrl();

        HttpClient httpClient = new HttpClient();

        // 模拟登陆，按实际服务器端要求选用 Post 或 Get 请求方式
        PostMethod postMethod = new PostMethod(loginUrl);

        // 设置登陆时要求的信息，用户名和密码
        NameValuePair[] data = {new NameValuePair("uname", name),
                new NameValuePair("upass", Util.getRSA(pswd))};
        postMethod.setRequestBody(data);
        try {
            // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
            httpClient.getParams().setCookiePolicy(
                    CookiePolicy.BROWSER_COMPATIBILITY);
            httpClient.executeMethod(postMethod);
            // 获得登陆后的 Cookie
            Cookie[] cookies = httpClient.getState().getCookies();
            StringBuffer tmpcookies = new StringBuffer();
            for (Cookie c : cookies) {
                tmpcookies.append(c.toString() + ";");
            }
            // 进行登陆后的操作1581,1602,1603,1610,1609,1608,1607,1606,1605,1620,1619,1617,1616,1622,1626,1642,1648,1647,1657
            GetMethod getMethod = new GetMethod(dataUrl);
            // 每次访问需授权的网址时需带上前面的 cookie 作为通行证
            getMethod.setRequestHeader("cookie", tmpcookies.toString());
            // 你还可以通过 PostMethod/GetMethod 设置更多的请求后数据
            // 例如，referer 从哪里来的，UA 像搜索引擎都会表名自己是谁，无良搜索引擎除外
            postMethod.setRequestHeader("Referer", "http://www.cc");
            postMethod.setRequestHeader("User-Agent", "www Spot");
            httpClient.executeMethod(getMethod);
            // 打印出返回数据，检验一下是否成功
            String text = getMethod.getResponseBodyAsString();
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
