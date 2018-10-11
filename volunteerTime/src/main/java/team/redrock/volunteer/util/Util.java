package team.redrock.volunteer.util;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import team.redrock.volunteer.config.VolunteerProperties;

public class Util {

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
        params.add("data", "-----BEGIN PUBLIC KEY-----\n" +
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbJ2QYNdiFlzE0mcyq7tcZc5dP\n" +
                "vof6696l2cJJM8kOxeXT8EonfvLzfsEGmwjNp3gvAyF14LvqT6w7oH40sFFnX358\n" +
                "Eb+HZXx6CZ4LOkaTW0KNS6yodsRv0uwJhFMwREqEVbqd6jcCxTGKDOieendC8x1f\n" +
                "sg3Muagyfawc+o+tewIDAQAB\n" +
                "-----END PUBLIC KEY-----^^^"+upass);
        params.add("data", VolunteerProperties.getRsa()+upass);
        params.add("type", "rsapubkey");
        params.add("arg", "pad=1_s=gb2312_t=0");
        //发送http请求并返回结果
        String str = HttpClient.client(url, method, params);

        JsonObject returnData = new JsonParser().parse(str).getAsJsonObject();
        String json = String.valueOf(returnData.get("data"));
        return json.substring(2, json.length()-2);
    }

}
