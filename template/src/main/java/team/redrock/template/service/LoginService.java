package team.redrock.template.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.redrock.template.dao.Dao;
import team.redrock.template.vo.User;

/**
 * Created by huangds on 2017/10/28.
 */
@Service
public class LoginService {

    @Autowired
    private Dao dao;

  public boolean verifyLogin(User user){

     User userList = this.dao.verityAccount(user);
     if (userList==null){
         return false;
     }else {
         return true;
     }
  }

}
