package team.redrock.cheerleaders.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import team.redrock.cheerleaders.util.AbstractBaseController;

@RestController
//@Controller
public class Control extends AbstractBaseController {
    @GetMapping("/hello")
    public String index(){
        String redirect = "http://2j2uew.natappfree.cc/info";
        String url = "https://wx.idsbllp.cn/MagicLoop/index.php?s=/addon/Api/Api/oauth&redirect="+redirect;

        RedirectView red = new RedirectView(url,true);

        return "hello";
    }

    @GetMapping("/info")
    public String open_id(String openid){
        System.out.println(openid);
        return "info";
    }
}
