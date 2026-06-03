package com.worldcup.tracker.controller;

import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.model.Prediction;
import com.worldcup.tracker.model.User;
import com.worldcup.tracker.repository.UserRepository;
import com.worldcup.tracker.service.MatchService;
import com.worldcup.tracker.service.PredictionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.List;

@Controller
public class MatchController {
    
    private final MatchService matchService;
    private final PredictionService predictionService;
    private final UserRepository userRepository;

    MatchController(MatchService matchService, PredictionService predictionService, UserRepository userRepository){
        this.matchService = matchService;
        this.predictionService = predictionService;
        this.userRepository = userRepository;
    }

    @GetMapping("/matches")
    public String matches(Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = getUser(userDetails);

        model.addAttribute("matches", matchService.getAllMatches());
        model.addAttribute("matchService", matchService);
        model.addAttribute("user", user);

        return "matches/list";
    }

    @GetMapping("/matches/{id}")
    public String matchDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getUser(userDetails);
        Match match = matchService.getMatchById(id);

        Optional<Prediction> userPrediction = predictionService.getPrediction(user, match);
        List<Prediction> visiblePredictions = predictionService.getVisiblePredictions(user, match);

        model.addAttribute("match", match);
        model.addAttribute("user", user);
        model.addAttribute("userPrediction", userPrediction);
        model.addAttribute("visiblePredictions", visiblePredictions);
        model.addAttribute("canEdit", predictionService.canEdit(match));
        model.addAttribute("canViewOthers", predictionService.canViewOthers(match));

        return "matches/detail";
    }

    private User getUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("Logged in user not found in database"));
    }
}
