package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.util.Util;
import team.redrock.running.vo.Record;

import java.util.List;

@Service
@Component
public class RecordServiceImp {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RecordDao recordDao;

    public JSONArray getLatLngList(String student_id,String pageParam, int num){
        int start = 0;
        int end = 14;
        Util.partPage(pageParam, start, end);
        List<Record> recordList = this.recordDao.selectDistanceRecordList(student_id);

        num = recordList.size();
        JSONArray jsonArray = new JSONArray();

        jsonArray.add(recordList);
        return jsonArray;
    }
    public Record getRecordById(String id){
        Record record = this.recordDao.selectDistanceRecordById(id);
        return record;
    }

}
