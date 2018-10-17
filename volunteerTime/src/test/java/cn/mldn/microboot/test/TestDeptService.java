package cn.mldn.microboot.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.redrock.volunteer.StartSpringBootMain;
import team.redrock.volunteer.service.impl.IServiceImp;
import team.redrock.volunteer.util.ReptileUtil;
import team.redrock.volunteer.vo.Record;

import java.util.List;

@SpringBootTest(classes = StartSpringBootMain.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestDeptService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private IServiceImp iServiceImp;
    @Test
    public void testList() throws Exception {
//        System.out.println(this.redisTemplate.opsForValue().get("2017211903"));
//        this.redisTemplate.delete("2017211903");

        List<Record> recordList =  ReptileUtil.detail("15223166166", "kk12345");

        if (recordList==null){
            System.out.println("null");
        }
    }

}
