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
    private Integer numberOfInnings;
    private Integer runsScored;
    private Integer ballsFaced;
    private Integer ballsBowled;
    private Integer runsConcede;
    private Double strikeRate;
    private Double battingAverage;
    private Double bowlingAverage;
    private Integer wicketsTaken ;
    private Double economy;
    private Integer hundreds;
    private Integer fifties;
    private Integer sixes;
    private Integer fours;

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
