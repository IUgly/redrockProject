package team.redrock.running.service;

import team.redrock.running.vo.RankInfo;
import team.redrock.running.vo.User;

public interface PositionRankInterface {
    RankInfo dayNumRankByStudentId(User user);

    RankInfo weekNumRankByStudentId(User user);

    RankInfo monthNumRankByStudentId(User user);
}
