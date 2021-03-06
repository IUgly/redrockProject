package team.redrock.volunteer.util;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.redrock.volunteer.config.Config;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Util {

    @Autowired
    private Config config;
    private static Config configDouble;

    @PostConstruct
    public void init(){
        configDouble = config;
    }

    public static String message(String code, String msg, double allHours, JsonArray record){
        JsonObject json =new JsonObject();
        json.addProperty("code", code);
        json.addProperty("msg", msg);
        json.addProperty("hours", allHours);

        json.add("record", record);

        return json.toString();
    }

    public static String assembling(int code, String msg, String data){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (data!=""){
            json.put("data", data);
        }
        return json.toString();
    }


    public static String login(String account, String password) throws Exception {

        String url = configDouble.getLoginUrl();

        Map<String, String> map= new HashMap<>();

        map.put("uname", account);
        map.put("upass", password);
        map.put("referer", "http%253A%252F%252Fwww.zycq.org%252Fapp%252Fuser%252Fhour.php");

        String str = send(url, map, "utf-8");

        JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
        String code = jsonObject.get("code").getAsString();
        return code;
    }

    public static String send(String url, Map<String,String> map, String encoding) throws ParseException, IOException {
        String body = "";

        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        //装填参数
        List<BasicNameValuePair> nvps = new ArrayList<>();
        if(map!=null){
            for (Map.Entry<String, String> entry : map.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        //设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));

        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = (String) EntityUtils.toString((org.apache.http.HttpEntity) entity, encoding);
        }
        EntityUtils.consume((org.apache.http.HttpEntity) entity);
        //释放链接
        response.close();
        return body;
    }

    public static String agentWhat(HttpServletRequest request, String plainText){
        String userPassword = request.getParameter(plainText);
        String userAgent = request.getHeader("user-agent");
        try {
            if (userAgent.contains("Android")){
                return Decrypt.aesDecryptString(userPassword);
            }else if (userAgent.contains("IPhone")){
                return AESCipher.aesEncryptString(userPassword, "redrockvolunteer");
            }else {
                return "error";
            }
        }catch (Exception e){
            return "error";
        }

    }

}
