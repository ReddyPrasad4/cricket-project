package com.cricket.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Matches {

    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String venue;
    private LocalTime startTime;
    private LocalTime endTime;
    private String winner;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToMany
    private List<Team> teams;

    @OneToMany(mappedBy = "matches" , cascade = CascadeType.ALL)
    private List<MatchStatistics> matchStatistics;

    @ManyToOne
    private Series series;

}
