package com.worldcup.tracker.service;

import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.model.Prediction;
import com.worldcup.tracker.model.User;
import com.worldcup.tracker.model.UserScore;
import com.worldcup.tracker.repository.PredictionRepository;
import com.worldcup.tracker.repository.UserScoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PredictionService {
    
    private final PredictionRepository predictionRepository;
    private final UserScoreRepository userScoreRepository;
    private final MatchService matchService;

    public PredictionService(PredictionRepository predictionRepository, UserScoreRepository userScoreRepository, MatchService matchService){
        this.predictionRepository = predictionRepository;
        this.userScoreRepository = userScoreRepository;
        this.matchService = matchService;
    }

    public boolean canEdit(Match match){
        return !matchService.isLocked(match);
    }

    public boolean canViewOthers(Match match){
        return !matchService.isLocked(match);
    }

    public Optional<Prediction> getPrediction(User user, Match match){
        return predictionRepository.findByUserAndMatch(user, match);
    }

    public List<Prediction> getVisiblePredictions(User user, Match match){
        if (canViewOthers(match)){
            return predictionRepository.findByMatch(match);
        }

        Optional<Prediction> own = predictionRepository.findByUserAndMatch(user, match);
        return own.map(List::of).orElse(List.of());
    }

    public Prediction savePrediction(User user, Match match, Integer homeScore, Integer awayScore){
        if (!canEdit(match)){
            throw new IllegalStateException("Predictions are now locked for this match. One hour or less to go!");
        }

        if (homeScore < 0 || awayScore < 0){
            throw new IllegalArgumentException("Scores cannot be a negative number");
        }

        Prediction prediction = predictionRepository.findByUserAndMatch(user, match).orElse(new Prediction());
        prediction.setUsername(user);
        prediction.setMatch(match);
        prediction.setHomeScore(homeScore);
        prediction.setAwayScore(awayScore);

        if (!userScoreRepository.existsByUser(user)){
            UserScore userScore = new UserScore();
            userScore.setUsername(user);
            userScoreRepository.save(userScore);
        }

        return predictionRepository.save(prediction);
    }

    public List<Prediction> getUserPredictions(User user){
        return predictionRepository.findByUser(user);
    }
}
