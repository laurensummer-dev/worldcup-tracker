package com.worldcup.tracker.repository;

import com.worldcup.tracker.model.Match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>{
    
    // Find all matches ordered by kick off time
    List<Match> findAllByOrderByKickOffTimeAsc();

    // Find matches by status
    List<Match> findByStatusOrderByKickOffTimeAsc(String status);

    // Find completed matches that haven't been scored yet
    List<Match> findByStatusAndScoredFalse(String status);

    // Find matches starting within the next hour
    @Query("SELECT m FROM Match m WHERE m.kickOffTime BETWEEN :now AND :oneHourFromNow")
    List<Match> findMatchesStartingSoon(
            @org.springframework.data.repository.query.Param("now")
            LocalDateTime now,
            @org.springframework.data.repository.query.Param("oneHourFromNow")
            LocalDateTime oneHourFromNow);
    
    // Find all matches for a specific group
    List<Match> findByGroupNameOrderByKickOffTimeAsc(String groupName);

    // Find all distinct group names that have fixtures
    @Query("SELECT DISTINCT m.groupName FROM Match m ORDER BY m.groupName ASC")
    List<String> findDistinctGroupNames();
}
