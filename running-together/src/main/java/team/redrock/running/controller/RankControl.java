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

    @GetMapping(value = "/rank_list", produces = "application/json")
    public String rankList(String time, String rank, Integer page){
        this.redisTemplate.delete(rank); //删除指定散列
        if (page == null){
            page = 1;
        }
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
