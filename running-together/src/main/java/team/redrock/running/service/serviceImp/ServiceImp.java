package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team.redrock.running.service.IService;
import team.redrock.running.vo.User;

import java.util.HashMap;
import java.util.Map;

@Service
@Component
public class ServiceImp implements IService {
    @Override
    public void hello() {
        System.out.println("hello");
    }

    @Override
    public User login(String student_id, String password) {
        RestTemplate restT = new RestTemplate();
        Map<String, String> map = new HashMap<>();
        map.put("stuNum",student_id);
        map.put("idNum", password);

        JSONObject responseEntity = restT.postForObject("http://hongyan.cqupt.edu.cn/api/verify", map, JSONObject.class);
        JSONObject json = responseEntity.getJSONObject("data");
        return new User(json);
    }
}
