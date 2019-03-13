package team.redrock.running.aop;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import team.redrock.running.service.serviceImp.UserServiceImp;
import team.redrock.running.vo.User;

//@Aspect
//@Component
public class ControllerAspect {
    @Autowired
    private UserServiceImp userServiceImp;
    public static final String UpdateRunDistanceMethod = "execution(public * team.redrock.running.controller.UserControl.update(..))";
//    @Pointcut("execution(public * team.redrock.running.controller.UserControl.update(..))")
//    public void updateRunDistance(){
//    }
    @After(value = UpdateRunDistanceMethod)
    public void updateAfter(JoinPoint joinPoint){
        Object[] obj = joinPoint.getArgs();
        for (Object argItem : obj) {
            if (argItem instanceof JSONObject) {
                JSONObject json = (JSONObject) argItem;
                User user = this.userServiceImp.selectUserInfo(json.getString("student_id"));
            }
        }

    }
}
