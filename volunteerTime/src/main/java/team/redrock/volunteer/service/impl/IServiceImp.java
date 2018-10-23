package team.redrock.volunteer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.redrock.volunteer.dao.Dao;
import team.redrock.volunteer.service.IService;
import team.redrock.volunteer.vo.Record;
import team.redrock.volunteer.vo.User;

import java.util.List;

@Service
public class IServiceImp implements IService {

    @Autowired
    private Dao dao;
    @Override
    public boolean insertRecord(Record record) {
        return this.dao.insertRecords(record);
    }

    @Override
    public List<Record> selectRecordList(String uid) {
        return this.dao.getRecordList(uid);
    }

    @Override
    public Boolean insertBind(User user) {
        return this.dao.bindInsert(user);
    }

    @Override
    public User selectUser(String uid) {
        return this.dao.selectUser(uid);
    }

    @Override
    public Boolean updateUser(User user) {
        return this.dao.updateUser(user);
    }

//    @Override
//    public String login(String account, String password) throws IOException {
//
//        String url = config.getLoginUrl();
//        HttpMethod method = HttpMethod.POST;
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("uname", account);
//        params.add("upass", Util.getRSA(password));
//        params.add("referer", "http%253A%252F%252Fwww.zycq.org%252Fapp%252Fuser%252Fhour.php");
//
//        Map<String, String> map= new HashMap<>();
//
//        map.put("uname", account);
//        map.put("upass", Util.getRSA(password));
//        map.put("referer", "http%253A%252F%252Fwww.zycq.org%252Fapp%252Fuser%252Fhour.php");
//
////        String str = HttpClient.client(url, method, params);
//        String str = send(url, map, "utf-8");
//
//        JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
//        String code = jsonObject.get("code").getAsString();
//        return code;
//    }

    @Override
    public Boolean deleteRecord(String uid) {
        return this.dao.deleteRecord(uid);
    }
}
