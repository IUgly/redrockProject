package team.redrock.volunteer.vo;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        User userOne = new User("2", "accOne", "pswdOne");
        User userTwo = new User("1", "accTwo", "pswdTwo");
        User userThree = new User("3", "accTh", "pswdTh");
        List webList = new ArrayList();

        webList.add(userOne);
        webList.add(userTwo);
        webList.add(userThree);

        Collections.sort(webList);

        Gson gson = new Gson();
        String json = gson.toJson(webList);

        System.out.println(json);
    }
}
