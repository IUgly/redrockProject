package team.redrock.cheerleaders.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class HttpUtil {
    public static Boolean judgingQualification(String openId){
        try {
            String url = "https://wx.idsbllp.cn/MagicLoop/index.php";

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("s", "/addon/UserCenter/UserCenter/getStuInfoByOpenId");
            map.add("openId", openId);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<JSONObject> response = restTemplate.postForEntity( url, request , JSONObject.class );
            String student_id = response.getBody().getString("student_id");

            return Double.valueOf(student_id)/1000000 > 2015;
        }catch (Exception e){
            return false;
        }

    }
}
