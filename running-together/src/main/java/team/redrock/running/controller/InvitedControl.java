package team.redrock.running.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import team.redrock.running.service.serviceImp.UserServiceImp;
import team.redrock.running.vo.User;

@RestController
public class InvitedControl {
    @Autowired
    private UserServiceImp userServiceImp;
    @PostMapping(value = "invite/update", produces = "application/json")
    public String Upload(String student_id, String[] Invitees){
        return "";
    }
    @PostMapping(value = "invite/searchinfo", produces = "application/json")
    public String searchInfo(String info){
        User user = this.userServiceImp.selectUserInfo(info);
        return "";
    }
}
