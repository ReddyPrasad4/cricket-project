package com.cricket.service.intrface;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.RankingsDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface RankingService {

    ResponseEntity<BaseResponseDTO> assignRankings(UUID seriesId) ;

    ResponseEntity<List<RankingsDTO>> getAllRankings();
}
