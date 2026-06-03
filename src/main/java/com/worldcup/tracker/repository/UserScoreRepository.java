package com.worldcup.tracker.repository;

import com.worldcup.tracker.model.User;
import com.worldcup.tracker.model.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserScoreRepository extends JpaRepository<UserScore, Long> {

    // Find a specific user's score record
    Optional<UserScore> findByUser(User user);

    // Find all scores ordered by points for leaderboard
    List<UserScore> findAllByOrderByTotalPointsDesc();

    // Check if a score record exists for a user
    boolean existsByUser(User user);
}