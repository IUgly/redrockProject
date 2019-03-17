package team.redrock.test;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.redrock.running.StartSpringBootMain;
import team.redrock.running.dao.RecordDao;
import team.redrock.running.dao.UserDao;
import team.redrock.running.dto.InvitationSend;
import team.redrock.running.service.IRankService;
import team.redrock.running.service.serviceImp.UpdateScoreService;
import team.redrock.running.util.SerializeUtil;
import team.redrock.running.vo.InviteInfo;
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
    private RedisTemplate redisTemplate;
    public static final String INVITATION_REDIS = "InvitationRedis011";
    public static final String SCORE_RANK = "dayRank";
    @Autowired
    private UpdateScoreService updateScoreService;
    @Autowired
    private RecordDao recordDao;
    @Autowired
    private IRankService iRankService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void testInvited(){
        InviteInfo inviteInfo = new InviteInfo("123321","crown");
        System.out.println("i1:" +inviteInfo.toString());
        String invited_id = inviteInfo.getInvited_id();
        String strInvitedInfo = SerializeUtil.serialize(inviteInfo);

        this.stringRedisTemplate.opsForValue().set(INVITATION_REDIS+invited_id, strInvitedInfo);
        InviteInfo inviteInfo2 = (InviteInfo) SerializeUtil.deserialize
                (this.stringRedisTemplate.opsForValue().get(INVITATION_REDIS+invited_id));
        System.out.println(inviteInfo2.getNickname());

    }

    @Test
    public void redisTest(){
        Gson gson = new Gson();
        String student_id = "2017211903";
        User user0 = this.userDao.selectUserByStudentId(student_id);

        String redis = "uiaos";
        this.redisTemplate.opsForHash().put(redis, student_id, user0);

        User user = (User) this.redisTemplate.opsForHash().get(redis, student_id);

        System.out.println("before:"+gson.toJson(user));

        InvitationSend invitationSend = new InvitationSend("kk");
        user.enQueueInvitation(invitationSend);
        System.out.println(new Gson().toJson(user.deQueueInvitation()));
        user.enQueueInvitation(invitationSend);
        System.out.println("After"+gson.toJson(user));

        this.redisTemplate.opsForHash().put(redis, student_id, user);

        User user2 = (User) this.redisTemplate.opsForHash().get(redis, student_id);

        System.out.println("In Redis:"+gson.toJson(user2));
    }

    @Test
    public void tableSize (){
        System.out.println(new Gson().toJson(
                this.recordDao.size(
                        "student_distance_rank", "day_distance",1)));
    }

    @Test
    public void rankInfo() throws Exception{


        this.redisTemplate.delete("student_invitation_rank"); //删除指定散列
        String result = (String) this.redisTemplate.opsForHash().get("student_invitation_rank", "1");
        if (result==null){
            System.out.println("result is null");

            String info = this.iRankService.rankList("months", "student_invitation_rank", 1);
            this.redisTemplate.opsForHash().put("student_invitation_rank", "1", new Gson().toJson(info));
            result = new Gson().toJson(info);
        }
        System.out.println(result);

    }

    @Test
    public void update(){
        Record record = new Record();
        record.setBegin_time(System.currentTimeMillis());
        record.setStudent_id("2017211903");
        record.setDate(Date.valueOf("2016-11-12"));
        record.setEnd_time(System.currentTimeMillis());
        JSONArray latLing = JSONArray.parseArray("[{\"longitude\":39.999391,\"latitude\":116.135972},{\"longitude\":39.999391,\"latitude\":116.135972},{\"longitude\":39.999391,\"latitude\":116.135972}]");
        record.setLat_lng(latLing);
        this.recordDao.insertDistanceRecord(record);
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
        String info = (String) this.redisTemplate.opsForValue().get("2017211903");
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
//        JSONArray jsonArray = new JSONArray();
//        while (it.hasNext()) {
//            ZSetOperations.TypedTuple str = it.next();
//            JSONObject json = JSONObject.parseObject(str.getValue().toString());
//            json.put("rank", start+1);
//            json.put("total", total);
//            jsonArray.add(json);
//            start++;
//        }
//        System.out.println(jsonArray.toJSONString());
//        System.out.println("获取到的排行和分数列表:" + gson.toJson(rangeWithScores));

        while (it.hasNext()){
            ZSetOperations.TypedTuple str = it.next();
            System.out.println(str.getValue());
        }
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
