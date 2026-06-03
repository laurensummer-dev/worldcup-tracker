package com.worldcup.tracker.service;

import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchService {
    
    private final MatchRepository matchRepository;
    private final ScoringService scoringService;

    public MatchService(MatchRepository matchRepository, ScoringService scoringService){
        this.matchRepository = matchRepository;
        this.scoringService = scoringService;
    }

    public Match enterResult(Long id, Integer homeScore, Integer awayScore){
        Match match = getMatchById(id);
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        matchRepository.save(match);

        // trigger scoring immediately
        scoringService.scoreMatch(match);

        return match;
    }

    public List<Match> getAllMatches(){
        return matchRepository.findAllByOrderByKickOffTimeAsc();
    }

    public Match getMatchById(Long id){
        return matchRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "Match not found: " + id
        ));
    }

    public List<Match> getMatchByStatus(String status){
        return matchRepository.findByStatusOrderByKickOffTimeAsc(status);
    }

    public Match createMatch(String homeTeam, String awayTeam, LocalDateTime kickOffTime){
        Match match = new Match();
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setKickOffTime(kickOffTime);
        return matchRepository.save(match);
    }

    public Match updateStatus(Long id, String status){
        Match match = getMatchById(id);
        match.setStatus(status);
        return matchRepository.save(match);
    }

    public Match updateResult(Long id, Integer homeScore, Integer awayScore){
        Match match = getMatchById(id);

        if (!match.getStatus().equals("COMPLETED")){
            match.setStatus("COMPLETED");
        }

        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        return matchRepository.save(match);
    }

    public boolean isLocked(Match match){
        return !match.getKickOffTime().isAfter(LocalDateTime.now().plusHours(1));
    }

    public List<Match> getUnscoredCompletedMatches(){
        return matchRepository.findByStatusAndScoredFalse("COMPLETED");
    }

    public void markAsScored(Match match){
        match.setScored(true);
        matchRepository.save(match);
    }
}
