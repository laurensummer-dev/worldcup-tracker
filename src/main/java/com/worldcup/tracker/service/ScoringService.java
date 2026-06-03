package com.worldcup.tracker.service;

import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.model.Prediction;
import com.worldcup.tracker.model.UserScore;
import com.worldcup.tracker.repository.MatchRepository;
import com.worldcup.tracker.repository.PredictionRepository;
import com.worldcup.tracker.repository.UserScoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScoringService {
    
    private static final int EXACT_SCORE_POINTS = 5;
    private static final int CORRECT_OUTCOME_POINTS = 2;

    private final PredictionRepository predictionRepository;
    private final UserScoreRepository userScoreRepository;
    private final MatchRepository matchRepository;

    public ScoringService(PredictionRepository predictionRepository, UserScoreRepository userScoreRepository, MatchRepository matchRepository){
        this.predictionRepository = predictionRepository;
        this.userScoreRepository = userScoreRepository;
        this.matchRepository = matchRepository;
    }

    @Transactional
    public void scoreMatch(Match match){
        List<Prediction> predictions = predictionRepository.findByMatch(match);

        for(Prediction prediction : predictions){
            int points = calculatePoints(prediction, match);
            prediction.setPointsAwarded(points);
            predictionRepository.save(prediction);
            updateUserScore(prediction, points);
        }

        match.setScored(true);
        matchRepository.save(match);
    }

    private int calculatePoints(Prediction prediction, Match match){
        int predictedHome = prediction.getHomeScore();
        int predictedAway = prediction.getAwayScore();
        int actualHome = match.getHomeScore();
        int actualAway = match.getAwayScore();

        if (predictedHome == actualHome && predictedAway == actualAway){
            return EXACT_SCORE_POINTS;
        }

        if (correctOutcome(predictedHome, predictedAway, actualHome, actualAway)){
            return CORRECT_OUTCOME_POINTS;
        }

        return 0;
    }

    private boolean correctOutcome(int predictedHome, int predictedAway, int actualHome, int actualAway){
        if (predictedHome > predictedAway && actualHome > actualAway){
            return true;
        }

        if (predictedAway > predictedHome && actualAway > actualHome){
            return true;
        }

        if (predictedHome == predictedAway && actualHome == actualAway){
            return true;
        }

        return false;
    }

    private void updateUserScore(Prediction prediction, int points){
        UserScore userScore = userScoreRepository
            .findByUser(prediction.getUser())
            .orElseThrow(() -> new IllegalStateException("No score record found for user: " + prediction.getUser().getUsername()));

        userScore.setTotalPoints(userScore.getTotalPoints() + points);

        if (points == EXACT_SCORE_POINTS){
            userScore.setCorrectScores(userScore.getCorrectScores() + 1);
            userScore.setCorretOutcomes(userScore.getCorrectOutcomes() + 1);
        } else if (points == CORRECT_OUTCOME_POINTS) {
            userScore.setCorrectScores(userScore.getCorrectOutcomes() + 1);
        }
        
        userScoreRepository.save(userScore);
    }
}
