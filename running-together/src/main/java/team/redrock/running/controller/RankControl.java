package team.redrock.running.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team.redrock.running.bean.ResponseBean;
import team.redrock.running.enums.UnicomResponseEnums;
import team.redrock.running.service.serviceImp.RecordServiceImp;
import team.redrock.running.service.serviceImp.PositionRankServiceImp;
import team.redrock.running.service.serviceImp.UpdateRunDataImp;
import team.redrock.running.service.serviceImp.UserServiceImp;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.User;

@RestController
public class RankControl {
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private UpdateRunDataImp updateRunDataImp;
    @Autowired
    private PositionRankServiceImp positionRankServiceImp;
    @Autowired
    private RecordServiceImp recordServiceImp;
    @GetMapping(value = "sanzou/rank/student/distance", produces = "application/json")
    public String getStuRankNum(String student_id, String kind_ran){
        User user = this.userServiceImp.selectUserInfo(student_id);
        if (user!=null){
            RankInfo rankInfo = this.positionRankServiceImp.dayNumRankByStudentId(user);
            return JSONObject.toJSONString(new ResponseBean<>(rankInfo, UnicomResponseEnums.SUCCESS));
        }else {
            return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.NO_USER_EXIST));
        }
    }

    //sanzou/{student_id}/list/{page?}
    @GetMapping(value = "sanzou/{student_id}/list", produces = "application/json")
    public String getStuRankNums(String student_id, String page){
        return "";
    }
}
