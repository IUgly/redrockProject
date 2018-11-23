package team.redrock.running.service;

import org.springframework.stereotype.Service;
import team.redrock.running.vo.User;

@Service
public interface IService {

    public void hello();

    public User login(String student_id, String password);
}
