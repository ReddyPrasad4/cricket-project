package com.cricket.service.intrface;


import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.MatchStatisticsDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface MatchStatisticsService {

     ResponseEntity<BaseResponseDTO> saveStatistics(MatchStatisticsDTO matchStatisticsDTO);
     ResponseEntity<List<MatchStatisticsDTO>> getAllStatisticsDetails();

     ResponseEntity<BaseResponseDTO> updateStatistics(MatchStatisticsDTO matchStatisticsDTO);

     // utility methods
     List<MatchStatisticsDTO> getPlayerSeriesStatistics(UUID playerId,UUID seriesId);
     MatchStatisticsDTO sumAllStatistics(List<MatchStatisticsDTO> matchStatisticsDTOList);

     // player every match statistics (list)
     ResponseEntity<List<MatchStatisticsDTO>> getEveryStatisticsByPlayerId(UUID playerId);

     // player over all statistics(single statistic)
     ResponseEntity<MatchStatisticsDTO> getOverAllStatisticsOfAPlayer(UUID playerId);

     // player Series statistics
     ResponseEntity<List<MatchStatisticsDTO>> getSeriesStatisticsOfAPlayer(UUID playerId, UUID seriesId);

}
