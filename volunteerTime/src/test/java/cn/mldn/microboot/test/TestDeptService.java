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

@SpringBootTest(classes = StartSpringBootMain.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestDeptService {
//
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private IServiceImp iServiceImp;

    @Test
    public void testList() throws Exception {
        System.out.println(this.redisTemplate.opsForValue().get("2017211903"));
        this.redisTemplate.delete("2017211903");
        System.out.println(this.redisTemplate.opsForValue().get("2017211903"));

//        String code = this.iServiceImp.login("15223166166", "kk123456");
//        if (!code.equals("0")){
//            System.out.println(Util.assembling("3", "该志愿者账号密码被修改，请重新绑定", ""));
//        }
//        System.out.println(Util.getRSA("kk123456"));
    }

}
