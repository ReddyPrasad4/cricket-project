package com.cricket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MatchStatistics {
    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    private int numberOfInnings;
    private int runsScored;
    private int ballsFaced;
    private int ballsBowled;
    private int runsConcede;
    private double strikeRate;
    private double battingAverage;
    private double bowlingAverage;
    private int wicketsTaken ;
    private double economy;
    private int hundreds;
    private int fifties;
    private int sixes;
    private int fours;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    private Matches matches;

    @ManyToOne
    private Player player;



}
