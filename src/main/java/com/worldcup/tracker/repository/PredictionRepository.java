package com.worldcup.tracker.repository;

import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.model.Prediction;
import com.worldcup.tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long>{

    // Find a specific user's prediction for a specific match
    Optional<Prediction> findByUserAndMatch(User user, Match match);

    // Find all predictions for a specific match (for viewing others)
    List<Prediction> findByMatch(Match match);

    // Find all predictions made by a specific user
    List<Prediction> findByUser(User user);

    // Check if a user has already predicted a match
    boolean existsByUserAndMatch(User user, Match match);

    // Find all predictions for a match that have been awarded points
    List<Prediction> findByMatchAndPointsAwardedGreaterThan(Match match, Integer points);
}
