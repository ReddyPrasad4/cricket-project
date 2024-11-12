package com.cricket.service.implementation;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.PlayerDTO;
import com.cricket.dto.TeamDTO;
import com.cricket.entity.Matches;
import com.cricket.entity.Player;
import com.cricket.entity.Team;
import com.cricket.repository.MatchRepository;
import com.cricket.repository.TeamRepository;
import com.cricket.service.intrface.TeamService;
import com.cricket.util.ApplicationConstants;
import com.cricket.util.Convertion;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TeamServiceImplementation implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private Convertion convertion;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public ResponseEntity<BaseResponseDTO> createTeam(TeamDTO teamDTO, UUID matchId) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            if (!ObjectUtils.isEmpty(teamDTO) && !ObjectUtils.isEmpty(matchId)) {
                Matches belongingMatch = matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException((ApplicationConstants.MATCH_NOT_FOUND)));
                Team inputTeam = convertion.convertToEntity(teamDTO, Team.class);
                inputTeam.setMatches(List.of(belongingMatch));
                teamRepository.save(inputTeam);
                response.setMessage(ApplicationConstants.TEAM_SAVED_SUCCESS);
                return new ResponseEntity<>(response, HttpStatus.CREATED);

            } else {
                response.setMessage(ApplicationConstants.NULL_INPUT);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            log.error(ApplicationConstants.ERROR_SAVING_TEAM, e.getLocalizedMessage());
            response.setMessage(e.getLocalizedMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        TeamDTO responseTeamDTO = new TeamDTO();
        try {
            List<Team> teamList = teamRepository.findAll();
            if (!CollectionUtils.isEmpty(teamList)) {
                List<TeamDTO> teamDTOList = teamList.stream().map(team -> convertion.convertToDto(team,TeamDTO.class)).toList();
                return new ResponseEntity<>(teamDTOList, HttpStatus.OK);
            }
            else {
                responseTeamDTO.setMessage(ApplicationConstants.EMPTY_OUTPUT);
                return new ResponseEntity<>(List.of(responseTeamDTO), HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.error(ApplicationConstants.ERROR_FETCHING_TEAMS, e.getMessage());
            responseTeamDTO.setMessage(ApplicationConstants.ERROR_FETCHING_TEAMS);
            return new ResponseEntity<>(List.of(responseTeamDTO), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<TeamDTO> getTeamById(UUID teamId) {
        TeamDTO responseTeamDTO = new TeamDTO();
        try {
            if (!ObjectUtils.isEmpty(teamId)) {
                Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException(ApplicationConstants.TEAM_NOT_FOUND));
                return new ResponseEntity<>(convertion.convertToDto(team,TeamDTO.class), HttpStatus.OK);
            }
            else {
                responseTeamDTO.setMessage(ApplicationConstants.NULL_INPUT);
                return new ResponseEntity<>(responseTeamDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error(ApplicationConstants.ERROR_FETCHING_TEAMS, e.getMessage());
            responseTeamDTO.setMessage(e.getLocalizedMessage());
            return new ResponseEntity<>(responseTeamDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<BaseResponseDTO> updateTeam(TeamDTO teamDTO) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            if (!ObjectUtils.isEmpty(teamDTO.getId()) && !ObjectUtils.isEmpty(teamDTO)) {
                Team existingTeam = teamRepository.findById(teamDTO.getId()).orElseThrow(() -> new RuntimeException(ApplicationConstants.TEAM_NOT_FOUND));
                Team updatedTeam = objectMapper.readerForUpdating(existingTeam).readValue(objectMapper.writeValueAsBytes(teamDTO));
                teamRepository.save(updatedTeam);
                response.setMessage(ApplicationConstants.TEAM_UPDATED_SUCCESS);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.setMessage(ApplicationConstants.NULL_INPUT);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error(ApplicationConstants.ERROR_FETCHING_TEAMS, e.getMessage());
            response.setMessage(e.getLocalizedMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<List<PlayerDTO>> getPlayersByTeamId(UUID teamId) {
        PlayerDTO responsePlayerDTO = new PlayerDTO();
        try {
            if (!ObjectUtils.isEmpty(teamId)) {
                Team retrievedTeam = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException(ApplicationConstants.TEAM_NOT_FOUND));
                if (!ObjectUtils.isEmpty(retrievedTeam)) {
                    List<Player> playerList = retrievedTeam.getPlayers();
                    if (!CollectionUtils.isEmpty(playerList)) {
                        List<PlayerDTO> playerDTOList = playerList.stream().map(player -> convertion.convertToDto(player, PlayerDTO.class)).toList();
                        return new ResponseEntity<>(playerDTOList, HttpStatus.OK);
                    }
                    responsePlayerDTO.setMessage(ApplicationConstants.EMPTY_OUTPUT);
                    return new ResponseEntity<>(List.of(responsePlayerDTO), HttpStatus.NO_CONTENT);
                }
            }
            responsePlayerDTO.setMessage(ApplicationConstants.NULL_INPUT);
            return new ResponseEntity<>(List.of(responsePlayerDTO), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            responsePlayerDTO.setMessage(e.getLocalizedMessage());
            return new ResponseEntity<>(List.of(responsePlayerDTO), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<List<PlayerDTO>> getPlayersByRole(String playerRole, UUID teamId) {
        PlayerDTO responsePlayerDTO = new PlayerDTO();
        try {
            if (!ObjectUtils.isEmpty(teamId) && !StringUtils.isEmpty(playerRole)) {
                Team retrievedTeam = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException(ApplicationConstants.PLAYER_NOT_FOUND));
                if (!ObjectUtils.isEmpty(retrievedTeam)) {
                    List<Player> playerList = retrievedTeam.getPlayers();
                    if (!CollectionUtils.isEmpty(playerList)) {
                        List<PlayerDTO> playerDTOList = playerList.stream().filter(player -> player.getRole().equalsIgnoreCase(playerRole)).map(player -> convertion.convertToDto(player, PlayerDTO.class)).toList();
                        return new ResponseEntity<>(playerDTOList, HttpStatus.OK);
                    }
                    responsePlayerDTO.setMessage(ApplicationConstants.EMPTY_OUTPUT);
                    return new ResponseEntity<>(List.of(responsePlayerDTO), HttpStatus.NO_CONTENT);
                }
            }
            responsePlayerDTO.setMessage(ApplicationConstants.NULL_INPUT);
            return new ResponseEntity<>(List.of(responsePlayerDTO), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            responsePlayerDTO.setMessage(e.getLocalizedMessage());
            return new ResponseEntity<>(List.of(responsePlayerDTO), HttpStatus.BAD_REQUEST);
        }
    }

}
