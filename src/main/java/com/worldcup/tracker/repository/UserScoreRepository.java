package com.worldcup.tracker.repository;

import com.worldcup.tracker.model.User;
import com.worldcup.tracker.model.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserScoreRepository extends JpaRepository<UserScore, Long> {

    Optional<UserScore> findByUser(User user);

    List<UserScore> findAllByOrderByTotalPointsDesc();

    boolean existsByUser(User user);
}