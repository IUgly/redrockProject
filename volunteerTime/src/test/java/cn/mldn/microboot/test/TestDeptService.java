package cn.mldn.microboot.test;

import com.google.gson.Gson;
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
import team.redrock.volunteer.util.ReptileUtil;
import team.redrock.volunteer.vo.Record;

import java.util.List;

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
    public void login() throws Exception {
        List<Record> recordList =  ReptileUtil.detail("15223166166", "kk123456");
        System.out.println(new Gson().toJson(recordList));
    }
    @Test
    public void test() throws Exception {
//        String str_plaintext = "kk123456";
//        System.err.println("明文："+str_plaintext);
//        byte[] bt_cipher = encrypt(str_plaintext.getBytes(), config.getStr_pubK());
//        System.out.println("加密后："+ Base64.encodeBase64String(bt_cipher));
//        String account = "15223166166";
//        String password = "kk123456";
//        String info = Util.login(account, password);
//        System.out.println(info);
//        byte[] bt = Base64.decodeBase64(Base64.encodeBase64String(bt_cipher));
//        byte[] bt_original = decrypt(bt_cipher, config.getStr_priK());
//        String str_original = new String(bt_original);
//        System.out.println("解密结果:"+str_original);
//        Util.messageDecrypt(Base64.encodeBase64String(bt_cipher));
    }



}
