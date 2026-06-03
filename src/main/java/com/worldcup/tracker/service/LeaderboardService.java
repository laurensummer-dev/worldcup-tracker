package com.worldcup.tracker.service;

import com.worldcup.tracker.model.UserScore;
import com.worldcup.tracker.repository.UserScoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardService {

    private final UserScoreRepository userScoreRepository;

    public LeaderboardService(UserScoreRepository userScoreRepository){
        this.userScoreRepository = userScoreRepository;
    }

    public List<UserScore> getLeaderBoard(){
        return userScoreRepository.findAllByOrderByTotalPointsDesc();
    }

    public UserScore getUserScore(com.worldcup.tracker.model.User user) {
        return userScoreRepository.findByUser(user).orElse(null);
    }

    public int getUserRank(com.worldcup.tracker.model.User user){
        List<UserScore> leaderboard = getLeaderBoard();

        for (int i = 0; i < leaderboard.size(); i++){
            if (leaderboard.get(i).getUser().getId().equals(user.getId())) {
                return i + 1;
            }
        }

        return -1; // Returns if user is not on the leaderboard yet.
    }
}
