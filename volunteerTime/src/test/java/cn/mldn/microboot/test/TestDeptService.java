package cn.mldn.microboot.test;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.redrock.volunteer.StartSpringBootMain;
import team.redrock.volunteer.config.Config;
import team.redrock.volunteer.service.impl.IServiceImp;

import static team.redrock.volunteer.util.Util.decrypt;
import static team.redrock.volunteer.util.Util.encrypt;

@SpringBootTest(classes = StartSpringBootMain.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestDeptService {
    @Autowired
    private Config config;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private IServiceImp iServiceImp;
    @Test
    public void test() throws Exception {
        String str_plaintext = "kk123456";
        System.err.println("明文："+str_plaintext);
        byte[] bt_cipher = encrypt(str_plaintext.getBytes(), config.getStr_pubK());
        System.out.println("加密后："+ Base64.encodeBase64String(bt_cipher));
//        String account = "15223166166";
//        String password = "kk123456";
//        String info = Util.login(account, password);
//        System.out.println(info);
//        byte[] bt = Base64.decodeBase64(Base64.encodeBase64String(bt_cipher));
        byte[] bt_original = decrypt(bt_cipher, config.getStr_priK());
        String str_original = new String(bt_original);
        System.out.println("解密结果:"+str_original);
//        Util.messageDecrypt(Base64.encodeBase64String(bt_cipher));
    }

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
