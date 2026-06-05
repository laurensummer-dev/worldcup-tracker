package com.worldcup.tracker.service;

import com.worldcup.tracker.model.Match;
import com.worldcup.tracker.repository.MatchRepository;
import com.worldcup.tracker.repository.PredictionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final PredictionRepository predictionRepository;
    private final ScoringService scoringService;

    @PersistenceContext
    private EntityManager entityManager;

    public MatchService(MatchRepository matchRepository,
                        PredictionRepository predictionRepository,
                        @Lazy ScoringService scoringService) {
        this.matchRepository = matchRepository;
        this.predictionRepository = predictionRepository;
        this.scoringService = scoringService;
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAllByOrderByKickOffTimeAsc();
    }

    public Match getMatchById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Match not found: " + id));
    }

    public List<Match> getMatchesByStatus(String status) {
        return matchRepository
                .findByStatusOrderByKickOffTimeAsc(status);
    }

    public List<Match> getMatchesByGroup(String groupName) {
        return matchRepository
                .findByGroupNameOrderByKickOffTimeAsc(groupName);
    }

    public List<String> getDistinctGroups() {
        return matchRepository.findDistinctGroupNames();
    }

    public Match createMatch(String homeTeam, String awayTeam,
                             LocalDateTime kickOffTime,
                             String groupName) {
        Match match = new Match();
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setKickOffTime(kickOffTime);
        match.setGroupName(groupName);
        return matchRepository.save(match);
    }

    @Transactional
    public Match updateStatus(Long id, String status) {
        Match match = getMatchById(id);

        if (!status.equals("COMPLETED") && match.getScored()) {
            scoringService.rescoreMatch(match);
            match = getMatchById(id);
        }

        match.setStatus(status);
        Match saved = matchRepository.save(match);
        entityManager.flush();
        entityManager.clear();

        return saved;
    }

    @Transactional
    public Match enterResult(Long id, Integer homeScore,
                             Integer awayScore) {
        Match match = getMatchById(id);
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.setStatus("COMPLETED");
        matchRepository.save(match);

        scoringService.rescoreMatch(match);

        return match;
    }

    @Transactional
    public Match updateGroup(Long id, String groupName) {
        Match match = getMatchById(id);
        match.setGroupName(groupName);
        return matchRepository.save(match);
    }

    public boolean isLocked(Match match) {
        if (!match.getStatus().equals("SCHEDULED")) {
            return true;
        }

        Optional<LocalDateTime> firstKickOff = getFirstKickOffTime();
        if (firstKickOff.isEmpty()) {
            return false;
        }
        return !firstKickOff.get()
                            .isAfter(LocalDateTime.now().plusHours(1));
    }

    public Optional<LocalDateTime> getFirstKickOffTime() {
        List<Match> matches = matchRepository
                .findAllByOrderByKickOffTimeAsc();
        if (matches.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(matches.get(0).getKickOffTime());
    }

    public void deleteMatch(Long id) {
        Match match = getMatchById(id);
        if (match.getScored()) {
            throw new IllegalStateException(
                    "Cannot delete a match that has already been scored");
        }
        predictionRepository.deleteByMatch(match);
        matchRepository.deleteById(id);
    }

    public boolean hasTournamentStarted() {
        Optional<LocalDateTime> firstKickOff = getFirstKickOffTime();
        if (firstKickOff.isEmpty()) {
            return false;
        }

        return firstKickOff.get().isBefore(LocalDateTime.now());
    }

    @Transactional
    public Match updateMatch(Long id, String homeTeam,
                            String awayTeam,
                            LocalDateTime kickOffTime,
                            String groupName, String status,
                            Integer homeScore, Integer awayScore) {

        Match match = getMatchById(id);
        boolean wasCompleted = match.getStatus().equals("COMPLETED");
        boolean isNowCompleted = status.equals("COMPLETED");
        boolean hasScores = homeScore != null && awayScore != null;
        boolean scoresCleared = wasCompleted && isNowCompleted
                && !hasScores && match.getScored();
        boolean scoresChanged = wasCompleted && isNowCompleted
                && hasScores && match.getScored()
                && match.getHomeScore() != null
                && (!match.getHomeScore().equals(homeScore)
                    || !match.getAwayScore().equals(awayScore));

        // Update basic fields
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setKickOffTime(kickOffTime);
        match.setGroupName(groupName);
        match.setStatus(status);

        if (isNowCompleted && hasScores) {
            match.setHomeScore(homeScore);
            match.setAwayScore(awayScore);
            matchRepository.save(match);

            if (scoresChanged) {
                // Result edited — rescore
                scoringService.rescoreMatch(match);
            } else if (!match.getScored()) {
                // First time scoring
                scoringService.scoreMatch(match);
            }

        } else if (isNowCompleted && scoresCleared) {
            // Scores cleared — reverse points and clear scores
            scoringService.rescoreMatch(match);
            match = getMatchById(id);
            match.setHomeScore(null);
            match.setAwayScore(null);
            match.setScored(false);
            matchRepository.save(match);

        } else if (!isNowCompleted && wasCompleted
                && match.getScored()) {
            // Reverted from COMPLETED — reverse points
            scoringService.rescoreMatch(match);
            match = getMatchById(id);
            match.setStatus(status);
            match.setHomeScore(null);
            match.setAwayScore(null);
            matchRepository.save(match);

        } else {
            matchRepository.save(match);
        }

        entityManager.flush();
        entityManager.clear();

        return getMatchById(id);
    }
}