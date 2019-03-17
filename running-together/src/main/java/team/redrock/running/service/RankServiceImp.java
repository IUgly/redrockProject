package team.redrock.running.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import team.redrock.running.bean.RankResponseBean;
import team.redrock.running.bean.ResponseBean;
import team.redrock.running.dao.RankDao;
import team.redrock.running.enums.UnicomResponseEnums;
import team.redrock.running.vo.RankInfo;

import java.util.ArrayList;
import java.util.List;

@Service
public class RankServiceImp implements IRankService {
    @Autowired
    private RankDao rankDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public String rankList(String type, String table, Integer page) {
        this.redisTemplate.delete(table);
        String result = (String) this.redisTemplate.opsForHash().get(table, page);
        if (result==null){
            List<RankInfo> info = new ArrayList<>();
            info = this.rankDao.rankList(type, table, page);
            this.redisTemplate.opsForHash().put(table, page, new Gson().toJson(info));
            result = new Gson().toJson(info);
            Integer sum = this.rankDao.rankNum(table);
            return JSONObject.toJSONString(new RankResponseBean(JSONObject.parse(result), UnicomResponseEnums.SUCCESS, sum, info.size()));
        }
        return result;
    }

    @Override
    public String rankPlace(String type, String table, String id) {
        this.redisTemplate.delete(table);
        RankInfo result = (RankInfo) this.redisTemplate.opsForHash().get(table+type, id);
        if (result==null){
            result = this.rankDao.rankPlace(type, table, id);
            this.redisTemplate.opsForHash().put(table+type, id, result);
        }
        return JSONObject.toJSONString(new ResponseBean<>(result, UnicomResponseEnums.SUCCESS));
    }
}
