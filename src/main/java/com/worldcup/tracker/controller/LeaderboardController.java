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
public class LeaderboardController extends BaseController {
    
    private final LeaderboardService leaderboardService;

    LeaderboardController(LeaderboardService leaderboardService, UserRepository userRepository){
        super(userRepository);
        this.leaderboardService = leaderboardService;
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
}
