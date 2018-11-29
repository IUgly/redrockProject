package team.redrock.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.redrock.running.StartSpringBootMain;
import team.redrock.running.service.serviceImp.RecordServiceImp;
import team.redrock.running.service.serviceImp.UpdateRunDataImp;
import team.redrock.running.service.serviceImp.UserServiceImp;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.Record;
import team.redrock.running.vo.User;

import java.sql.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


@SpringBootTest(classes = StartSpringBootMain.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestDeptService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

//    public static final String SCORE_RANK = "test29";

    public static final String SCORE_RANK = "dayRank";
    @Autowired
    private UpdateRunDataImp updateRunDataImp;
    @Autowired
    private RecordServiceImp recordServiceImp;
    @Autowired
    private UserServiceImp userServiceImp;
    @Test
    public void update(){
        Record record = new Record();
        record.setBegin_time(System.currentTimeMillis());
        record.setStudent_id("2017211903");
        record.setDate(Date.valueOf("2016-11-12"));
        Gson gson = new Gson();
        record.setEnd_time(System.currentTimeMillis());
        this.updateRunDataImp.notInvitedUpdate(record);
//        String recordId = String.valueOf(this.latLngServiceImp.selectIdOfRecord(record));
//        JSONArray latLing = json.getJSONArray("lat_lng");
//        this.latLngServiceImp.insertLatLngToRedis(recordId, json.getString("lat_lng"));
//        record.setLat_lng(latLing);
        RankInfo rankInfo = new RankInfo(record);
        this.updateRunDataImp.insertOnceDayRunToRedis(rankInfo);

        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = redisTemplate.opsForZSet().reverseRangeWithScores(SCORE_RANK, 0, 10);
        System.out.println("获取到的排行和分数列表:" + gson.toJson(rangeWithScores));
    }
    @Test
    public void UserTest(){
        User user = new User();
        user.setNickname("testname");
        user.setStudent_id("2017211900");
        user.setCollege("jisuanji");
        user.setClass_id("41909291");
        Gson gson = new Gson();
//        this.redisTemplate.opsForValue().set("testa", gson.toJson(user));
//        System.out.println(this.redisTemplate.opsForValue().get("testa"));
//        this.userServiceImp.insertUserToRedis(user.getStudent_id(), user);
        String info = this.redisTemplate.opsForValue().get("2017211903");
        System.out.println(info);
        User u = gson.fromJson(info, User.class);
        System.out.println(u.toString());
    }

    @Test
    public void test() throws Exception {

        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            DefaultTypedTuple<String> tuple = new DefaultTypedTuple<>(""+(char)Math.random()*26+'A'+i, Math.random()*10000+i);
            tuples.add(tuple);
        }
        System.out.println("循环时间:" +( System.currentTimeMillis() - start));
        Long num = this.redisTemplate.opsForZSet().add(SCORE_RANK, tuples);
        System.out.println("批量新增时间:" +(System.currentTimeMillis() - start));
        System.out.println("受影响行数：" + num);
    }


    /**
     * 获取排行列表
     */
    @Test
    public void list() {

        int start = 0;
        int end = 10;

        Set<String> range = redisTemplate.opsForZSet().reverseRange(SCORE_RANK, 0, 10);

//        System.out.println("获取到的排行列表:" + gson.toJson(range));
        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = redisTemplate.opsForZSet().reverseRangeWithScores(SCORE_RANK, 0, 10);
//        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = redisTemplate.opsForZSet().rangeByScoreWithScores(SCORE_RANK, 0, 10);
        Long total = this.redisTemplate.opsForZSet().zCard(SCORE_RANK);
        Iterator<ZSetOperations.TypedTuple<String>> it = rangeWithScores.iterator();
        JSONArray jsonArray = new JSONArray();
        while (it.hasNext()) {
            ZSetOperations.TypedTuple str = it.next();
            JSONObject json = JSONObject.parseObject(str.getValue().toString());
            json.put("rank", start+1);
            json.put("total", total);
            jsonArray.add(json);
            start++;
        }
        System.out.println(jsonArray.toJSONString());
//        System.out.println("获取到的排行和分数列表:" + gson.toJson(rangeWithScores));

    }

    /**
     * 单个新增
     */
    @Test
    public void add() {
        redisTemplate.opsForZSet().add(SCORE_RANK, "张三", 19866);
    }
    /**
     * 获取单个的排行
     */

    /**
     * 统计两个分数之间的人数
     */
    @Test
    public void count(){
        Long count = redisTemplate.opsForZSet().count(SCORE_RANK, 8001, 9000);
        System.out.println("统计8001-9000之间的人数:" + count);
    }

    /**
     * 使用加法操作分数
     */
    @Test
    public void incrementScore(){
        Double score = redisTemplate.opsForZSet().incrementScore(SCORE_RANK, "牛七", 10000);
        System.out.println("牛7分数+1000后：" + score);

    }
    @Test
    public void data(){

    }

}