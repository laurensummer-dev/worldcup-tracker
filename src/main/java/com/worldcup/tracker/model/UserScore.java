package com.worldcup.tracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="user_scores")
public class UserScore {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name="total_points", nullable = false)
    private Integer totalPoints;

    @Column(name="correct_scores", nullable = false)
    private Integer correctScores;

    @Column(name="correctOutcomes", nullable = false)
    private Integer correctOutcomes;

    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        updatedAt = LocalDateTime.now();
        totalPoints = 0;
        correctScores = 0;
        correctOutcomes = 0;
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

    public Integer getTotalPoints(){
        return totalPoints;
    }
    public void setTotalPoints(Integer totalPoints){
        this.totalPoints = totalPoints;
    }

    public Integer getCorrectScores(){
        return correctScores;
    }
    public void setCorrectScores(Integer correctScores){
        this.correctScores = correctScores;
    }

    public Integer getCorrectOutcomes(){
        return correctOutcomes;
    }
    public void setCorretOutcomes(Integer correctOutcomes){
        this.correctOutcomes = correctOutcomes;
    }

    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt){
        this.updatedAt = updatedAt;
    }
}
