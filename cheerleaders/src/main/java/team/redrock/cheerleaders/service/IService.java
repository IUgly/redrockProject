package team.redrock.cheerleaders.service;

import org.springframework.stereotype.Service;
import team.redrock.cheerleaders.vo.User;

import java.util.List;
import java.util.Map;

@Service
//@Component
public interface IService {
    Map<String, List<User>> usersOrderByCollege();
}
