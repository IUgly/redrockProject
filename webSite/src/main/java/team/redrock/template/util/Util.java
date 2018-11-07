package team.redrock.template.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Util {


    /*
    修改推荐网址某一一级标签的网址
     */
    public static String alterWebs(String result, JSONObject req){

        JSONObject jsonObject = req.getJSONObject("info");
        String kindname = jsonObject.getString("kindname");
        String kind = null;
        JSONObject json = null;


//        String result = this.iServiceImp.selectInfo("");
        JSONArray jsonArray = JSONArray.parseArray(result);
        for (int i=0; i< jsonArray.size(); i++){

            json = jsonArray.getJSONObject(i);

            kind =json.getString("kindname");
            if (kind.equals(kindname)){
                jsonArray.remove(jsonArray.get(i));
                jsonArray.add(jsonObject);
            }
        }
        System.out.println(jsonArray.toJSONString());

        return jsonArray.toJSONString();
    }

    /**
     *
     * @param kind  数据库中某kindname
     * @param kindnames  接收参数中的kindname数组
     * @return
     */
    public static boolean isTrue(String kind, String[] kindnames){
        for (int j =0; j<kindnames.length; j++){
            if (kind.equals(kindnames[j])){
                return true;
            }
        }
        return false;
    }

    public static boolean isTrueFromType(String typeOfDataBase, String[] typesOfParam){
        for (int i=0; i<typesOfParam.length; i++){
            if (typeOfDataBase.equals(typesOfParam[i])){
                return true;
            }
        }
        return false;
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
    public static String resp(int code, String msg, JSONArray jsonArray){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (jsonArray.size()!=0){
            json.put("data", jsonArray);
        }
        return json.toString();
    }

}
