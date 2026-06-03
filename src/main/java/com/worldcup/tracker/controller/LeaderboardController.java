package com.worldcup.tracker.controller;

import com.worldcup.tracker.model.User;
import com.worldcup.tracker.model.UserScore;
import com.worldcup.tracker.repository.UserRepository;
import com.worldcup.tracker.service.LeaderboardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LeaderboardController {
    
    private final LeaderboardService leaderboardService;
    private final UserRepository userRepository;

    LeaderboardController(LeaderboardService leaderboardService, UserRepository userRepository){
        this.leaderboardService = leaderboardService;
        this.userRepository = userRepository;
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model, @AuthenticationPrincipal UserDetails userDetails){

        User user = getUser(userDetails);

        List<UserScore> leaderboard = leaderboardService.getLeaderBoard();
        UserScore userScore = leaderboardService.getUserScore(user);
        int userRank = leaderboardService.getUserRank(user);

        model.addAttribute("leaderboard", leaderboard);
        model.addAttribute("userScore", userScore);
        model.addAttribute("userRank", userRank);
        model.addAttribute("user", user);

        return "/leaderboard/index";
    }

    private User getUser(UserDetails userDetails){
        return userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException(
                "Logged in user not found in database"
            ));
    }
}
