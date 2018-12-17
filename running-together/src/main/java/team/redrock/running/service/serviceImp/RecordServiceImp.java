package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.util.Util;
import team.redrock.running.vo.Record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Component
public class RecordServiceImp {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RecordDao recordDao;
    @Autowired
    private UserServiceImp userServiceImp;
    public static final String LAT_LNG_REDIS = "latLngRedis";
    public static final String RECORD_REDIS = "recordRedis";

    public JSONArray getLatLngList(String student_id,String pageParam, int num){
        int start = 0;
        int end = 14;
        Util.partPage(pageParam, start, end);
        List<Record> recordList = this.recordDao.selectDistanceRecordList(student_id);

        num = recordList.size();
        JSONArray jsonArray = new JSONArray();
        for (int i=start; i<end; i++){
            String latLng = (String) this.redisTemplate.opsForHash().get(LAT_LNG_REDIS , recordList.get(i).getId());
            recordList.get(i).setLat_lng(JSONArray.parseArray(latLng));
        }
        jsonArray.add(recordList);
        return jsonArray;
    }
    public Record getRecordById(String id){
        Record record = this.recordDao.selectDistanceRecordById(id);
        record.setLat_lng(JSONArray.parseArray((String)this.redisTemplate.opsForHash().get(RECORD_REDIS, id)));
        return record;
    }
    public void putRedisHash(String key, Object o, String RedisHash){
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put(key, o);
        this.redisTemplate.opsForHash().putAll(RedisHash, hashMap);
        System.out.println(this.redisTemplate.opsForHash().get(RedisHash, key));
    }
}
