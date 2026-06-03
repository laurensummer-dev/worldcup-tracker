package com.worldcup.tracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="predictions")
public class Prediction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="match_id", nullable = false)
    private Match match;

    @Column(name="home_score", nullable = false)
    private Integer homeScore;

    @Column(name="away_score", nullable = false)
    private Integer awayScore;

    @Column(name="points_awarded", nullable = false)
    private Integer pointsAwarded;

    @Column(name="created_at", nullable=false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        pointsAwarded = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser(){
        return user;
    }
    public void setUsername(User user){
        this.user = user;
    }

    public Match getMatch(){
        return match;
    }
    public void setMatch(Match match){
        this.match = match;
    }

    public Integer getHomeScore(){
        return homeScore;
    }
    public void setHomeScore(Integer homeScore){
        this.homeScore = homeScore;
    }

    public Integer getAwayScore(){
        return awayScore;
    }
    public void setAwayScore(Integer awayScore){
        this.awayScore = awayScore;
    }

    public Integer getPointsAwarded(){
        return pointsAwarded;
    }
    public void setPointsAwarded(Integer pointsAwarded){
        this.pointsAwarded = pointsAwarded;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt){
        this.updatedAt = updatedAt;
    }
}
