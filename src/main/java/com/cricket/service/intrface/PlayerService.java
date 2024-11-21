package com.cricket.service.intrface;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.MatchStatisticsDTO;
import com.cricket.dto.PlayerDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface PlayerService {

     ResponseEntity<BaseResponseDTO> savePlayer(PlayerDTO playerDTO);
     ResponseEntity<BaseResponseDTO> saveListOfPlayers(List<PlayerDTO> playerDTOList,UUID teamId);
     ResponseEntity<List<PlayerDTO>> getAllDetails();
     ResponseEntity<PlayerDTO> getPlayerById(UUID playerId);
     ResponseEntity<BaseResponseDTO> updatePlayer(PlayerDTO playerDTO);

     ResponseEntity<List<MatchStatisticsDTO>> getPlayerMatchStatistics(UUID playerId);

}
