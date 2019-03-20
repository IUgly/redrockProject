package cn.mldn.microboot.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import team.redrock.volunteer.StartSpringBootMain;
import team.redrock.volunteer.config.Config;
import team.redrock.volunteer.dao.Dao;
import team.redrock.volunteer.service.impl.IServiceImp;

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
    @Autowired
    private Dao dao;

    @Test
    public void test2(){

        StringBuffer url = new StringBuffer();
        url.append("http://www.zycq.org/app/api/ver2.0.php?");
        url.append("os=3&");
        url.append("v=3&");
        url.append("m=login&");
        url.append("uname=15223166166&");
        url.append("upass=kk123456");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
//        map.add("os", "3");
//        map.add("v",  "3");
//        map.add("m", "login");
//        map.add("uname", "15223166166");
//        map.add("upass", "kk123456");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url.toString(), request , String.class );
        JSONObject json = JSONObject.parseObject(response.getBody());
        String c = json.getString("c");
        if (!c.equals("0")){
            System.out.println("密码错误");
        }
        String uid = json.getJSONObject("d").getString("uid");

        StringBuffer selectUrl = new StringBuffer();
        selectUrl.append("http://www.zycq.org/app/api/ver2.0.php?");
        selectUrl.append("os=3&");
        selectUrl.append("v=3&");
        selectUrl.append("id=").append(uid).append("&");
        selectUrl.append("p=1&");
        selectUrl.append("m=hour_vol");
        System.out.println(selectUrl.toString());

        RestTemplate restTemplate2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map2= new LinkedMultiValueMap<>();
//        map2.add("os", "3");
//        map2.add("v", "3");
//        map2.add("id", uid);
//        map2.add("p", "1");
//        map2.add("m", "hour_vol");

        HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<>(map, headers2);
        ResponseEntity<String> response2 = restTemplate2.postForEntity( selectUrl.toString(), request2 , String.class );
        JSONObject jsonObject = JSONObject.parseObject(response2.getBody());
        System.out.println(jsonObject);
        JSONArray out = jsonObject.getJSONObject("d").getJSONArray("list");

    }
    @Test
    public void login() throws Exception {
//        List<Record> recordList =  ReptileUtil.detail("15223166166", "kk123456");
//        System.out.println(new Gson().toJson(recordList));
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
