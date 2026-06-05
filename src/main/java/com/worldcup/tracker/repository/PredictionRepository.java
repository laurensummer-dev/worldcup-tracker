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

    Optional<Prediction> findByUserAndMatch(User user, Match match);

    List<Prediction> findByMatch(Match match);

    List<Prediction> findByUser(User user);

    boolean existsByUserAndMatch(User user, Match match);

    List<Prediction> findByMatchAndPointsAwardedGreaterThan(Match match, Integer points);

    void deleteByMatch(Match match);
}
