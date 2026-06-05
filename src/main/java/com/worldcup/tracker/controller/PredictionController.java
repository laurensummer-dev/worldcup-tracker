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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PredictionController extends BaseController{

    private final PredictionService predictionService;
    private final MatchService matchService;

    public PredictionController(PredictionService predictionService,
                                MatchService matchService,
                                UserRepository userRepository) {
        super(userRepository);
        this.predictionService = predictionService;
        this.matchService = matchService;
    }

    @PostMapping("/matches/{id}/predict")
    public String submitPrediction(
            @PathVariable Long id,
            @RequestParam Integer homeScore,
            @RequestParam Integer awayScore,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        User user = getUser(userDetails);
        Match match = matchService.getMatchById(id);

        try {
            predictionService.savePrediction(
                    user, match, homeScore, awayScore);

            if ("XMLHttpRequest".equals(requestedWith)) {
                Prediction prediction = predictionService
                        .getPrediction(user, match)
                        .orElse(null);

                model.addAttribute("match", match);
                model.addAttribute("user", user);
                model.addAttribute("prediction", prediction);
                model.addAttribute("predictionService", predictionService);
                model.addAttribute("matchService", matchService);

                return "matches/group-fixtures :: matchRow";
            }
            return "redirect:/matches/" + id + "?success=true";
        } catch (IllegalStateException e) {
            return "redirect:/matches/" + id + "?error=locked";
        } catch (IllegalArgumentException e) {
            return "redirect:/matches/" + id + "?error=invalid";
        }
    }
}