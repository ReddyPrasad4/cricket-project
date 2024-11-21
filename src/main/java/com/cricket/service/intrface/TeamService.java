package com.cricket.service.intrface;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.PlayerDTO;
import com.cricket.dto.TeamDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface TeamService {


    ResponseEntity<BaseResponseDTO> createTeam(TeamDTO teamDTO);

    ResponseEntity<List<TeamDTO>> getAllTeams();

    ResponseEntity<TeamDTO> getTeamById(UUID teamId);

    ResponseEntity<BaseResponseDTO> updateTeam(TeamDTO teamDTO);

    ResponseEntity<List<PlayerDTO>> getPlayersByTeamId(UUID teamId);

    ResponseEntity<List<PlayerDTO>> getPlayersByRole(String playerRole, UUID teamId);
}
