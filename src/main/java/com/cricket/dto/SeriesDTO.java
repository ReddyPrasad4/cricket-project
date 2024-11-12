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
public class SeriesDTO extends BaseResponseDTO {

    private UUID id;
    private String format;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String winner;

    List<MatchesDTO> matchesDTOList;

}
