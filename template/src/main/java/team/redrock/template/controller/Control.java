package team.redrock.template.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team.redrock.template.util.AbstractBaseController;

@RestController
public class Control extends AbstractBaseController {
    @GetMapping("/hello")
    public String index(){
        return "hello";
    }
}
