package team.redrock.cheerleaders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.redrock.cheerleaders.dao.Dao;
import team.redrock.cheerleaders.vo.User;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ServiceImp implements IService {
    @Autowired
    private Dao dao;

    @Override
    public Map<String, List<User>> usersOrderByCollege() {
        Function<User, String> collegeFilter = (user) -> {
            return user.getCollege();
        };
        return  this.dao.users()
                        .stream()
                        .collect(Collectors.groupingBy(collegeFilter));
    }
}
