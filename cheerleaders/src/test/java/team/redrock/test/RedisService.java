package team.redrock.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.redrock.cheerleaders.StartSpringBootMain;
import team.redrock.cheerleaders.vo.College;
import team.redrock.cheerleaders.vo.Colleges;

import java.util.*;

@SpringBootTest(classes = StartSpringBootMain.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    public static final String SCORE_RANK = "score_rank011";

    public static final String VOTE_RANK = "vote_rank011";

    /**
     * 批量新增
     */
    @Test
    public void Test(){
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        for (Colleges e: Colleges.values()
             ) {
            DefaultTypedTuple<String> tuple =
                    new DefaultTypedTuple<>(String.valueOf(e) , 2.0);
            tuples.add(tuple);
        }
        redisTemplate.opsForZSet().add(SCORE_RANK, tuples);
        redisTemplate.opsForZSet().add(VOTE_RANK, tuples);

    }


    //
    @Test
    public void List(){
        List<College> collegeList = new ArrayList<>();

        Arrays.stream(Colleges.values()).forEach(v -> {
            String collegeName = String.valueOf(v);
            Double score = redisTemplate.opsForZSet().score(SCORE_RANK, collegeName);
            Double vote = redisTemplate.opsForZSet().score(VOTE_RANK, collegeName);
            College college = new College(collegeName, score, vote);

            collegeList.add(college);
        });


//        collegeList.stream().reduce(0, (acc, e) -> acc + e.getInteger()).intValue;
//
//        int sum1 = list.stream().reduce(0, (acc, e) -> acc + e).intValue();
//
//        System.out.println(sum);

    }

    public College createCollegeObj(String collegeName){
        Double score = redisTemplate.opsForZSet().score(SCORE_RANK, collegeName);
        System.out.println("分数:" + score);

        Double vote = redisTemplate.opsForZSet().score(VOTE_RANK, collegeName);
        System.out.println("vote:" + score);

        return new College(collegeName, score, vote);
    }


    public void AddCollege(){
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        DefaultTypedTuple<String> tuple = new DefaultTypedTuple<>("计算机" , Math.random());
        tuples.add(tuple);
        this.redisTemplate.opsForZSet().add(SCORE_RANK, tuples);
        this.redisTemplate.opsForZSet().add(VOTE_RANK, tuples);

        //加分数
        this.redisTemplate.opsForZSet().incrementScore(SCORE_RANK, "计算机", 1);

        //加票数
        this.redisTemplate.opsForZSet().incrementScore(VOTE_RANK, "计算机", 1);

        //获得分数
        Double score = redisTemplate.opsForZSet().score(SCORE_RANK, "计算机");

        //获得票数
        Double vote= redisTemplate.opsForZSet().score(SCORE_RANK, "计算机");

        //获得所有列表
        Set<ZSetOperations.TypedTuple<String>> rangeWithScores =
                redisTemplate.opsForZSet().reverseRangeWithScores(SCORE_RANK, 0, 10);
        System.out.println("获取到的排行和分数列表:" + JSON.toJSONString(rangeWithScores));

    }

    /**
     * 获取排行列表
     */
    @Test
    public void list() {

        Set<String> range = redisTemplate.opsForZSet().reverseRange(SCORE_RANK, 0, 10);
        System.out.println("获取到的排行列表:" + JSON.toJSONString(range));
        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = redisTemplate.opsForZSet().reverseRangeWithScores(SCORE_RANK, 0, 10);
        System.out.println("获取到的排行和分数列表:" + JSON.toJSONString(rangeWithScores));

    }

    /**
     * 单个新增
     */
    @Test
    public void add() {
        redisTemplate.opsForZSet().add(SCORE_RANK, "李四", 8899);
    }

    /**
     * 获取单个的排行
     */
    @Test
    public void find(){
        Long rankNum = redisTemplate.opsForZSet().reverseRank(SCORE_RANK, "李四");
        System.out.println("李四的个人排名：" + rankNum);

        Double score = redisTemplate.opsForZSet().score(SCORE_RANK, "李四");
        System.out.println("李四的分数:" + score);
    }

    /**
     * 统计两个分数之间的人数
     */
    @Test
    public void count(){
        Long count = redisTemplate.opsForZSet().count(SCORE_RANK, 8001, 9000);
        System.out.println("统计8001-9000之间的人数:" + count);
    }

    /**
     * 获取整个集合的基数(数量大小)
     */
    @Test
    public void zCard(){
        Long aLong = redisTemplate.opsForZSet().zCard(SCORE_RANK);
        System.out.println("集合的基数为：" + aLong);
    }

    /**
     * 使用加法操作分数
     */
    @Test
    public void incrementScore(){
        Double score = redisTemplate.opsForZSet().incrementScore(SCORE_RANK, "李四", 1000);
        System.out.println("李四分数+1000后：" + score);
    }
}
