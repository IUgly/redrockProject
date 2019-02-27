package team.redrock.running.service.serviceImp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.util.Util;
import team.redrock.running.vo.ClassRank;
import team.redrock.running.vo.User;

import java.util.Iterator;
import java.util.Set;

@Service
@Component
public class RankListServerImp {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private ClassService classService;

    public int rankListNum(String kindRank){
        return Math.toIntExact(this.redisTemplate.opsForZSet().zCard(kindRank));
    }

    private Set<ZSetOperations.TypedTuple<String>> getRedisRangeWithScoreByPartPage(String pageParam, String kindRank){
        int start = 0;
        int end = 14;
        Util.partPage(pageParam, start, end);
        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = this.redisTemplate.opsForZSet().reverseRangeWithScores
                (kindRank, start, end);
        return rangeWithScores;
    }
    private String traversalPersonZSet(Set<ZSetOperations.TypedTuple<String>> rangeWithScores){
        Iterator<ZSetOperations.TypedTuple<String>> it = rangeWithScores.iterator();
        int rankStart = 0;
        JSONArray jsonArray = new JSONArray();
        while (it.hasNext()) {
            ZSetOperations.TypedTuple str = it.next();
            User user = this.userServiceImp.selectUserInfo(str.getValue().toString());
            JSONObject json = JSONObject.parseObject(user.toString());
            json.put("rank", rankStart+1);
            json.put("total", str.getScore());
            jsonArray.add(json);
            rankStart++;
        }
        return jsonArray.toString();
    }

    public String getPersonRankInvite(String pageParam, String kindRank){
        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = this.getRedisRangeWithScoreByPartPage(pageParam, kindRank);
        return traversalPersonZSet(rangeWithScores);
    }
    public String getPersonRankDistance(String pageParam, String kindRank){
        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = this.getRedisRangeWithScoreByPartPage(pageParam, kindRank);
        return traversalPersonZSet(rangeWithScores);
    }
    public void databaseToRedisStuDistance(){

    }

    private String traversalClassZSet(Set<ZSetOperations.TypedTuple<String>> rangeWithScores){
        Iterator<ZSetOperations.TypedTuple<String>> it = rangeWithScores.iterator();
        int rankStart = 0;
        JSONArray jsonArray = new JSONArray();
        while (it.hasNext()) {
            ZSetOperations.TypedTuple str = it.next();
            ClassRank classRank = this.classService.selectClassById(str.getValue().toString());
            JSONObject json = new JSONObject();
            json.put("class_id", str.getValue());
            json.put("total", str.getScore());
            json.put("duration", classRank.getDuration());
            json.put("college", classRank.getCollege());
            json.put("rank", ++rankStart);
            jsonArray.add(json);
        }
        return jsonArray.toString();
    }
    public String getClassRankInvited(String pageParam, String kindRank){
        Set<ZSetOperations.TypedTuple<String>> rangeWithScore = this.getRedisRangeWithScoreByPartPage(pageParam, kindRank);
        return traversalClassZSet(rangeWithScore);
    }
    public String getClassRankDistance(String pageParam, String kindRank){
        Set<ZSetOperations.TypedTuple<String>> rangeWithScore = this.getRedisRangeWithScoreByPartPage(pageParam, kindRank);
        return traversalClassZSet(rangeWithScore);
    }

}
