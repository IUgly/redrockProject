package team.redrock.running.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import team.redrock.running.WebSecurityConfig;
import team.redrock.running.service.LoginService;
import team.redrock.running.vo.User;

import javax.servlet.http.HttpSession;

/**
 * Created by huangds on 2017/10/24.
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/info")
    public String index(@SessionAttribute(WebSecurityConfig.SESSION_KEY)String account, Model model){

        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "没登陆";
    }

    @GetMapping("/loginVerify")
    public String loginVerify(String username,String password,HttpSession session){
        User user = new User();
        user.setName(username);
        user.setPassword(password);

        boolean verify = loginService.verifyLogin(user);
        if (verify) {
            session.setAttribute(WebSecurityConfig.SESSION_KEY, username);
            return "登陆成功";
        } else {
            return "密码错误";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute(WebSecurityConfig.SESSION_KEY);
        return "loginOut";
    }
}
