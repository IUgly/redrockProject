package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.vo.Record;

import java.util.List;

@Service
@Component
public class RecordServiceImp {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RecordDao recordDao;
    @Autowired
    private UserServiceImp userServiceImp;
    @Async
    public void insertLatLngToRedis(String recordId, String latLng) {
        this.redisTemplate.opsForValue().set(recordId, latLng);
    }
    public JSONArray getLatLngList(String student_id,String pageParam, int num){
        int page = 1;
        if (pageParam!=null){
            page = Integer.valueOf(pageParam);
        }
        int start = 0;
        int end = 14;
        if (page >1){
            start = 15*(page-1);
            end = start+14;
        }

        List<Record> recordList = this.recordDao.selectLatLngList(student_id);
        num = recordList.size();
        JSONArray jsonArray = new JSONArray();
        for (int i=start; i<end; i++){
            String latLng = this.redisTemplate.opsForValue().get(recordList.get(i).getId());
            recordList.get(i).setLat_lng(JSONArray.parseArray(latLng));
        }
        jsonArray.add(recordList);
        return jsonArray;
    }

    public String selectLatLngById(String id) {
        return this.redisTemplate.opsForValue().get(id);
    }
}
