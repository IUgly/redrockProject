package team.redrock.test;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.redrock.cheerleaders.StartSpringBootMain;
import team.redrock.cheerleaders.dao.Dao;
import team.redrock.cheerleaders.service.IService;
import team.redrock.cheerleaders.vo.College;
import team.redrock.cheerleaders.vo.User;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;


@SpringBootTest(classes = StartSpringBootMain.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestDeptService {
    @Autowired
    private Dao dao;
    @Autowired
    private IService iService;
    Map<String, List<User>> userByCollege
            = this.iService.usersOrderByCollege();

    public static Map<String,List<User>> rewordList(Map<String, List<User>> allUserList){
        Map<String, List<User>> result = new HashMap<>();
        allUserList.forEach((collegeName, userList) ->
                result.put(collegeName,
                        OneCollegeRewordUser(userList, new College(collegeName))));
        return result;
    }

    private static List<User> OneCollegeRewordUser
            (List<User> userList, College college) {
            List<User> rewordUsers = new ArrayList<>();
            ThreadLocalRandom random = ThreadLocalRandom.current();
            for (int j = 0; j < 100*college.getRate(); j++) {
                Integer index = random.nextInt(0, userList.size());
                rewordUsers.add(userList.get(index));
            }
            return rewordUsers;
    }

    @Test
    public void data(){
        Gson gson = new Gson();
        Map<String, List<User>> userByCollege
                = this.iService.usersOrderByCollege();

        Map<String, List<User>> rewordUsers = rewordList(userByCollege);

        System.out.println(gson.toJson(rewordUsers));
    }

}
