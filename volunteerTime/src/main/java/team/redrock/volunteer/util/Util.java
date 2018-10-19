package team.redrock.volunteer.util;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import team.redrock.volunteer.config.Config;
import team.redrock.volunteer.config.VolunteerProperties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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

    public static String assembling(String code, String msg, String data){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (data!=""){
            json.put("data", data);
        }
        return json.toString();
    }

    public static String getRSA(String upass){
        String url = "http://tool.chacuo.net/cryptrsapubkey";
        HttpMethod method =HttpMethod.POST;
        // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
//        params.add("data", VolunteerProperties.getRsa()+upass);
        params.add("data", configDouble.getRsa() + upass);
        params.add("type", "rsapubkey");
        params.add("arg", "pad=1_s=gb2312_t=0");
        //发送http请求并返回结果
        String str = HttpClient.client(url, method, params);

        JsonObject returnData = new JsonParser().parse(str).getAsJsonObject();
        String json = String.valueOf(returnData.get("data"));
        return json.substring(2, json.length()-2);
    }

}
