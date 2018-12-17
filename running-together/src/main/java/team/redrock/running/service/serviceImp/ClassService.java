package team.redrock.running.service.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.redrock.running.dao.ClassDao;
import team.redrock.running.vo.ClassRank;

@Service
@Component
public class ClassService {
    public static final String CLASS_REDIS = "ClassRedis";
    @Autowired
    private ClassDao classDao;
    @Autowired
    private RedisTemplate redisTemplate;

    public ClassRank selectClassById(String class_id){
        ClassRank classRank = (ClassRank) this.redisTemplate.opsForHash().get(CLASS_REDIS, class_id);
        if (classRank==null){
            return this.classDao.selectClassByClassId(class_id);
        }
        return classRank;
    }

}
