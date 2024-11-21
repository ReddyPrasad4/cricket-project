package com.cricket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamDTO  extends BaseResponseDTO{
    private UUID id;
    private String name;
    private Integer wins;
    private Integer losses;

    private List<MatchesDTO> matchesDTOList;

    private List<PlayerDTO> playerDTOList;

}
