package team.redrock.running.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team.redrock.running.bean.RankResponseBean;
import team.redrock.running.bean.ResponseBean;
import team.redrock.running.enums.UnicomResponseEnums;
import team.redrock.running.service.serviceImp.*;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.User;

@RestController
public class RankControl {
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private PositionRankServiceImp positionRankServiceImp;
    @Autowired
    private RankListServerImp rankListServerImp;
    @Autowired
    private RecordServiceImp recordServiceImp;
    @GetMapping(value = "sanzou/rank/student/distance", produces = "application/json")
    public String getStuRankNum(String student_id, String kind_rank){
        kind_rank = kind_rank + "StuDistance000";
        User user = this.userServiceImp.selectUserInfo(student_id);
        if (user!=null){
            RankInfo rankInfo = this.positionRankServiceImp.NumRankByStudentId(user, kind_rank);
            return JSONObject.toJSONString(new ResponseBean<>(rankInfo, UnicomResponseEnums.SUCCESS));
        }else {
            return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.NO_USER_EXIST));
        }
    }
    @GetMapping(value = "sanzou/ranklist/student/distance", produces = "application/json")
    public String getStuRankList(String page,String kind_rank){
        kind_rank = kind_rank + "StuDistance000";
        int num = this.rankListServerImp.rankListNum(kind_rank);
        JSONArray jsonArray = JSONArray.parseArray(this.rankListServerImp.getPersonRankDistance(page,kind_rank));
        return JSONObject.toJSONString(new RankResponseBean(jsonArray,UnicomResponseEnums.SUCCESS,num));
    }
    @GetMapping(value = "/sanzou/rank/class/distance/{kind_rank}/{page?}", produces = "application/json")
    public String getClaRankList(String page,String kindRank){
        int num = this.rankListServerImp.rankListNum(kindRank);
        JSONArray jsonArray = JSONArray.parseArray(this.rankListServerImp.getPersonRankDistance(page, kindRank));
        return JSONObject.toJSONString(new RankResponseBean(jsonArray, UnicomResponseEnums.SUCCESS,num));
    }
    @GetMapping(value = "/rank/invite/student", produces = "application/json")
    public String getStuInvitedRankList(String page, String kindRank){
        int num = this.rankListServerImp.rankListNum(kindRank);
        JSONArray jsonArray = JSONArray.parseArray(this.rankListServerImp.getPersonRankInvite(page, kindRank));
        return JSONObject.toJSONString(new RankResponseBean(jsonArray, UnicomResponseEnums.SUCCESS, num));
    }
    @GetMapping(value = "/rank/invite/class", produces = "application/json")
    public String getClaInvitedRankList(String page, String kindRank){
        int num = this.rankListServerImp.rankListNum(kindRank);
        JSONArray jsonArray = JSONArray.parseArray(this.rankListServerImp.getPersonRankInvite(page, kindRank));
        return JSONObject.toJSONString(new RankResponseBean(jsonArray, UnicomResponseEnums.SUCCESS, num));
    }
}
