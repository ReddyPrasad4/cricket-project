package com.cricket.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Player {
    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    private String name;
    private LocalDate bornDate;
    private String role;
    private String battingStyle;
    private String bowlingStyle;
    private String nationality;
    private Integer age;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    private Team team;

    @OneToMany(mappedBy = "player" ,cascade =CascadeType.ALL)
    private List<MatchStatistics> matchStatistics;


    @OneToOne(mappedBy = "player")
    private Rankings rankings;


}
