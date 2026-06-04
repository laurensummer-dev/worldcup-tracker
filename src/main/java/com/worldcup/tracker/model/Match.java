package com.worldcup.tracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class Match {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="home_team", nullable=false)
    private String homeTeam;

    @Column(name="away_team", nullable=false)
    private String awayTeam;

    @Column(name="kick_off_time", nullable=false)
    private LocalDateTime kickOffTime;

    @Column(name="home_score")
    private Integer homeScore;

    @Column(name="away_score")
    private Integer awayScore;

    @Column(name="status", nullable=false)
    private String status;

    @Column(name="scored", nullable=false)
    private Boolean scored;

    @Column(name="created_at", nullable=false, updatable=false)
    private LocalDateTime createdAt;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        status = "SCHEDULED";
        scored = false;
    }

    public Long getId(){ 
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getHomeTeam(){
        return homeTeam; 
    }
    public void setHomeTeam(String homeTeam){ 
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam(){
        return awayTeam;
    }
    public void setAwayTeam(String awayTeam){
        this.awayTeam = awayTeam;
    }

    public LocalDateTime getKickOffTime(){
        return kickOffTime;
    }
    public void setKickOffTime(LocalDateTime kickOffTime) {
        this.kickOffTime = kickOffTime;
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

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public Boolean getScored(){
        return scored;
    }
    public void setScored(Boolean scored){
        this.scored = scored;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getGroupName(){
        return groupName;
    }
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
}
