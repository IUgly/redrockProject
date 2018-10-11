package cn.mldn.microboot.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.redrock.volunteer.StartSpringBootMain;

@SpringBootTest(classes = StartSpringBootMain.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestDeptService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Test
    public void testList() throws Exception {
        System.out.println(this.redisTemplate.opsForValue().get("2017211903"));
        this.redisTemplate.delete("2017211903");
        System.out.println(this.redisTemplate.opsForValue().get("2017211903"));
    }

}
