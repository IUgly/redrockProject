package team.redrock.template.aop;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.redrock.template.service.serviceImp.IServiceImp;
import team.redrock.template.vo.Photo;

@Aspect
@Component
public class ControllerAspect {
    @Autowired
    private IServiceImp iServiceImp;
    public static final String UpdateRunDistanceMethod = "execution(public * team.redrock.template.controller.Control.edit(..))";
//    @Pointcut("execution(public * team.redrock.running.controller.UserControl.update(..))")
//    public void updateRunDistance(){
//    }
    @After(value = UpdateRunDistanceMethod)
    public void updateAfter(JoinPoint joinPoint){
        Object[] obj = joinPoint.getArgs();
        for (Object argItem : obj) {
            if (argItem instanceof JSONObject) {
                JSONObject json = (JSONObject) argItem;
                String title = json.getString("title");
                switch (title) {
                    case "recomweblist" : this.iServiceImp.insertAllWebsToFile();
                    case "hotweblist" : this.iServiceImp.insertHotWebsToFile();
                    case "picture" : this.iServiceImp.insertAboutsToFile(new Photo(json.getString("type")));
                }

            }
        }
    }
}