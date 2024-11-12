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

    private MatchesDTO matchesDTO;

    private PlayerDTO playerDTO;

}
