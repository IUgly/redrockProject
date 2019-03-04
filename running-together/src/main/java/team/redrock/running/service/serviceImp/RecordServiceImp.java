package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.bean.RankResponseBean;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.enums.UnicomResponseEnums;
import team.redrock.running.vo.Record;

import java.util.List;

@Service
@Component
public class RecordServiceImp {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RecordDao recordDao;

    public String getLatLngList(String student_id,Integer pageParam){

        List<Record> recordList = this.recordDao.selectDistanceRecordList(
                student_id,
                pageParam,
                "distance_record");

        Integer sum = this.recordDao.recordSize("distance_record", student_id);
        Integer this_pageSize = recordList.size();
        JSONArray jsonArray = new JSONArray();

        jsonArray.add(recordList);

        return JSONObject.toJSONString(new RankResponseBean(
                jsonArray,
                UnicomResponseEnums.SUCCESS,
                sum,
                this_pageSize));
    }
    public Record getRecordById(String id){
        Record record = this.recordDao.selectDistanceRecordById(id);
        return record;
    }

}
