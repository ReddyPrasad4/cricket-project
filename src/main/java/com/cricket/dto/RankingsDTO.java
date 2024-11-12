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
public class RankingsDTO extends BaseResponseDTO{

    private UUID id;
    private int ranking;
    private Boolean isDeleted;

    private PlayerDTO playerDTO;
}
