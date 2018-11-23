package team.redrock.running.util;

import com.alibaba.fastjson.JSONObject;

public class Util {
    public static String response(String status, String message, JSONObject json){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", status);
        jsonObject.put("message", message);
        jsonObject.put("data", json);
        return jsonObject.toString();

    }
}
