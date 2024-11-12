package com.cricket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerDTO extends BaseResponseDTO{
    private UUID id;
    private String name;
    private LocalDate bornDate;
    private String role;
    private String battingStyle;
    private String bowlingStyle;
    private String nationality;
    private int age;

    private TeamDTO teamDTO;

    private List<MatchStatisticsDTO> matchStatisticsDTOList;


    private RankingsDTO rankingsDTO;
}
