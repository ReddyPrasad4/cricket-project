package com.cricket.service.intrface;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.MatchStatisticsDTO;
import com.cricket.dto.MatchesDTO;
import com.cricket.dto.TeamDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface MatchesService {

    ResponseEntity<BaseResponseDTO> saveMatches(MatchesDTO matchesDTO);
    ResponseEntity<BaseResponseDTO> saveMatchByTeamsId(MatchesDTO matchesDTO, UUID firstTeamId, UUID secondTeamId);

    ResponseEntity<List<MatchesDTO>> getAllMatches();

    ResponseEntity<MatchesDTO> getMatchesById(UUID id);

    ResponseEntity<BaseResponseDTO> updateMatches( MatchesDTO matchesDTO);

    ResponseEntity<List<TeamDTO>> getTeamsByMatchId(UUID matchId);

    ResponseEntity<List<MatchStatisticsDTO>> getMatchStatisticsByMatchId(UUID matchId);


}