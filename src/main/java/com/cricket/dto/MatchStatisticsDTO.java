package com.cricket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchStatisticsDTO extends BaseResponseDTO{
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

    private MatchesDTO matchesDTO;

    private PlayerDTO playerDTO;

}
