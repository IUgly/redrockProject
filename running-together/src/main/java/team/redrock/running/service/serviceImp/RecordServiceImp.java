package team.redrock.running.service.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.vo.Record;

@Service
@Component
public class RecordServiceImp {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RecordDao recordDao;
    @Async
    public void insertLatLngToRedis(String recordId, String latLng) {
        this.redisTemplate.opsForValue().set(recordId, latLng);
    }

    public int selectIdOfRecord(Record record) {
        return this.recordDao.selectIdOfRecord(record);
    }

    public String selectLatLngById(String id) {
        return this.redisTemplate.opsForValue().get(id);
    }
}
