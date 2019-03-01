package team.redrock.running.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team.redrock.running.bean.RankResponseBean;
import team.redrock.running.bean.ResponseBean;
import team.redrock.running.enums.UnicomResponseEnums;
import team.redrock.running.service.IRankService;
import team.redrock.running.service.serviceImp.*;
import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.User;

@RestController
public class RankControl {
    @Autowired
    private IRankService iRankService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private PositionRankServiceImp positionRankServiceImp;
    @Autowired
    private RankListServerImp rankListServerImp;
    @Autowired
    private RecordServiceImp recordServiceImp;
    @GetMapping(value = "/rank/student/distance", produces = "application/json")
    public String getStuRankNum(String student_id, String kind_rank){
        kind_rank = kind_rank + "StuDistance002";
        User user = this.userServiceImp.selectUserInfo(student_id);
        if (user!=null){
            RankInfo rankInfo = this.positionRankServiceImp.NumRankByStudentId(user, kind_rank);
            if (rankInfo!=null){
                JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.NOT_POSITION));
            }
            return JSONObject.toJSONString(new ResponseBean<>(rankInfo, UnicomResponseEnums.SUCCESS));
        }else {
            return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.NO_USER_EXIST));
        }
    }
    @GetMapping(value = "/rank/class/distance", produces = "application/json")
    public String getClaRankNum(String class_id, String kind_rank){
        kind_rank = kind_rank + "ClaDistance002";
        RankInfo rankInfo = this.positionRankServiceImp.numRankByClass_id(class_id, kind_rank);
        if (rankInfo != null){
            return JSONObject.toJSONString(new ResponseBean<>(rankInfo, UnicomResponseEnums.SUCCESS));
        }else {
            return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.NO_USER_EXIST));
        }
    }
    @GetMapping(value = "/rank/student/invited", produces = "application/json")
    public String getInvitedRank(String student_id, String kind_rank){
        kind_rank = kind_rank + "StuInvited002";

        int num = this.rankListServerImp.rankListNum(kind_rank);
        User user = this.userServiceImp.selectUserInfo(student_id);
        if (user!=null){
            RankInfo rankInfo = this.positionRankServiceImp.NumRankByStudentId(user, kind_rank);
            return JSONObject.toJSONString(new ResponseBean<>(rankInfo, UnicomResponseEnums.SUCCESS));
        }else {
            return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.NO_USER_EXIST));
        }
    }
    @GetMapping(value = "ranklist/student/distance", produces = "application/json")
    public String getStuRankList(String page,String kind_rank){
        kind_rank = kind_rank + "StuDistance002";
        int num = this.rankListServerImp.rankListNum(kind_rank);
        JSONArray jsonArray = JSONArray.parseArray(this.rankListServerImp.getPersonRankDistance(page,kind_rank));
        return JSONObject.toJSONString(new RankResponseBean(jsonArray,UnicomResponseEnums.SUCCESS,num));
    }
    @GetMapping(value = "/ranklist/class/distance", produces = "application/json")
    public String getClaRankList(String page,String kind_rank){
        kind_rank = kind_rank + "ClaDistance002";
        int num = this.rankListServerImp.rankListNum(kind_rank);
        JSONArray jsonArray = JSONArray.parseArray(this.rankListServerImp.getClassRankDistance(page, kind_rank));
        return JSONObject.toJSONString(new RankResponseBean(jsonArray, UnicomResponseEnums.SUCCESS,num));
    }
    @GetMapping(value = "/rank/invite/student", produces = "application/json")
    public String getStuInvitedRankList(String page, String kind_rank){
        kind_rank = kind_rank + "StuInvited002";
        int num = this.rankListServerImp.rankListNum(kind_rank);
        JSONArray jsonArray = JSONArray.parseArray(this.rankListServerImp.getPersonRankInvite(page, kind_rank));
        return JSONObject.toJSONString(new RankResponseBean(jsonArray, UnicomResponseEnums.SUCCESS, num));
    }

    @GetMapping(value = "/rank_list", produces = "application/json")
    public String rankList(String time, String rank, Integer page){
        this.redisTemplate.delete(rank); //删除指定散列

        String result = this.iRankService.rankList(time, rank, page);
        if (result!=null){
            return result;
        }
        return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.NOT_POSITION));
    }

    @GetMapping(value = "/rank", produces = "application/json")
    public String rank(String time, String rank, String id){
        this.redisTemplate.delete(rank+time); //删除指定散列

        String result = this.iRankService.rankPlace(time,
                        rank,
                        id);
        if (result!=null){
            return result;
        }
        return JSONObject.toJSONString(new ResponseBean<>(UnicomResponseEnums.NOT_POSITION));
    }

}
