package team.redrock.running.controller;


import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import team.redrock.running.bean.ResponseBean;
import team.redrock.running.enums.UnicomResponseEnums;
import team.redrock.running.factory.ProxyFactory;
import team.redrock.running.service.IService;
import team.redrock.running.service.serviceImp.ServiceImp;
import team.redrock.running.util.AbstractBaseController;
import team.redrock.running.util.Token;
import team.redrock.running.vo.User;

import java.util.Date;

@RestController
public class Control extends AbstractBaseController {

    private IService iService = new ServiceImp();

    private IService proxy = (IService) new ProxyFactory(iService).getProxyInstance();

    @GetMapping("/hello")
    public String index(){
        return "hello";
    }
    @PostMapping(value = "/login", produces = "application/json")
    public String login(String student_id, String password){
        User user = this.iService.login(student_id, password);
        if (user!=null){
            Token token = new Token("2017211903", new Date());
            user.setToken(token.CreateToken());
            return JSONObject.toJSONString(new ResponseBean<>(user, UnicomResponseEnums.SUCCESS));
        }else {
            return JSONObject.toJSONString(new ResponseBean<>(
                    UnicomResponseEnums.INVALID_PASSWORD));
        }
    }
}
