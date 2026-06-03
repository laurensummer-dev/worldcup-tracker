package com.worldcup.tracker.controller;

import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.model.User;
import com.worldcup.tracker.repository.UserRepository;
import com.worldcup.tracker.service.MatchService;
import com.worldcup.tracker.service.PredictionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PredictionController {
    
    private final PredictionService predictionService;
    private final MatchService matchService;
    private final UserRepository userRepository;

    PredictionController(PredictionService predictionService, MatchService matchService, UserRepository userRepository){
        this.predictionService = predictionService;
        this.matchService = matchService;
        this.userRepository = userRepository;
    }

    @PostMapping("/matches/{id}/predict")
    public String submitPrediction(
            @PathVariable Long id,
            @RequestParam Integer homeScore,
            @RequestParam Integer awayScore,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        User user = getUser(userDetails);
        Match match = matchService.getMatchById(id);

        try {
            predictionService.savePrediction(user, match, homeScore, awayScore);
            return "redirect:/matches/" + id + "?success=true";
        } catch (IllegalStateException e) {
            return "redirect:/matches/" + id + "?error=invalid";
        }
    }

    private User getUser(UserDetails userDetails){
        return userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("Logged in user not found in database"));
    }
}
