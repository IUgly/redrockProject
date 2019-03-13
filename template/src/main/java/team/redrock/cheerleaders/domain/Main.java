package team.redrock.cheerleaders.domain;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        Runnable r = ()->{
            for(int x=0; x<50; x++){
                try {
                    http();
//                    tcp();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r).start();
    }

    public static void http() throws IOException {
        String url = "https://betterzzx.com/cqupt/getStu?name=陈";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("name", "李");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<JSONObject> response = restTemplate.postForEntity( url, request , JSONObject.class );

        System.out.println(response.getBody().getString("data"));
    }

    public static void tcp() throws IOException {
        String ip="https://betterzzx.com/cqupt/getStu?name=陈";   //服务器端ip地址
        int port=80;        //端口号
        Socket sck=new Socket(ip,port);
        //2.传输内容
        String content="#$%";
        byte[] bstream=content.getBytes("GBK");  //转化为字节流
        OutputStream os=sck.getOutputStream();   //输出流
        os.write(bstream);
        //3.关闭连接
        sck.close();
    }
}
