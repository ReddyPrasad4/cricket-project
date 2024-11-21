package com.cricket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TeamScoreDTO extends BaseResponseDTO {

    private String teamName;
    private int totalScore;
}
