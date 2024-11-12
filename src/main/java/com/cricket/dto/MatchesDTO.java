package com.cricket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchesDTO  extends BaseResponseDTO{
    private UUID id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String venue;
    private LocalTime startTime;
    private LocalTime endTime;
    private String winner;

    private List<TeamDTO> teamsDTO;

    private List<MatchStatisticsDTO> matchStatisticsDTOList;

    private SeriesDTO seriesDTO;

}
