package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.vo.User;

import java.util.Iterator;
import java.util.Set;

@Service
@Component
public class RankListServerImp {
    public static final String SCORE_RANK = "dayRankTEST";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserServiceImp userServiceImp;

    public int rankListNum(){
        return Math.toIntExact(this.redisTemplate.opsForZSet().zCard(SCORE_RANK));
    }

    public String getDayRankDistance(String pageParam){
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
        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = this.redisTemplate.opsForZSet().reverseRangeWithScores
                (SCORE_RANK, start, end);
        Iterator<ZSetOperations.TypedTuple<String>> it = rangeWithScores.iterator();
        int rankStart = 0;
        JSONArray jsonArray = new JSONArray();
        while (it.hasNext()) {
            ZSetOperations.TypedTuple str = it.next();
            User user = this.userServiceImp.selectUserInfo(str.getValue().toString());
            if (user==null){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("student_id", str.getValue());
                jsonObject.put("nickname", "无名氏"+rankStart+"号");
                jsonObject.put("rank", rankStart+1);
                jsonObject.put("total", str.getScore());
                jsonArray.add(jsonObject);
                rankStart++;
            }
            else {
                System.out.println(user.toString());
                com.alibaba.fastjson.JSONObject json = JSONObject.parseObject(user.toString());
                json.put("rank", rankStart+1);
                json.put("total", str.getScore());
                jsonArray.add(json);
                rankStart++;
            }
        }
        return jsonArray.toString();
    }
}
