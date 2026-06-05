package com.worldcup.tracker.repository;

import com.worldcup.tracker.model.Match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>{
    
    List<Match> findAllByOrderByKickOffTimeAsc();
    
    List<Match> findByGroupNameOrderByKickOffTimeAsc(String groupName);

    @Query("SELECT DISTINCT m.groupName FROM Match m ORDER BY m.groupName ASC")
    List<String> findDistinctGroupNames();
}
