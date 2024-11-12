package com.cricket.service.implementation;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.MatchStatisticsDTO;
import com.cricket.dto.PlayerDTO;
import com.cricket.entity.MatchStatistics;
import com.cricket.entity.Player;
import com.cricket.repository.PlayerRepository;
import com.cricket.service.intrface.PlayerService;
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

@Slf4j
@Service
public class PlayerServiceImplementation implements PlayerService {

    @Autowired
    private PlayerRepository playersRepository;

    @Autowired
    private Convertion convertion;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public ResponseEntity<BaseResponseDTO> savePlayer(PlayerDTO playerDTO) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            if (!ObjectUtils.isEmpty(playerDTO) && !StringUtils.isEmpty(playerDTO.getName()) && (!StringUtils.isEmpty(playerDTO.getBattingStyle()) || !StringUtils.isEmpty(playerDTO.getBowlingStyle()))) {
                playersRepository.save(convertion.convertToEntity(playerDTO, Player.class));
                responseDTO.setMessage(ApplicationConstants.PLAYER_SAVED_SUCCESS);
            } else {
                responseDTO.setMessage(ApplicationConstants.NULL_INPUT);
            }
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            responseDTO.setMessage(ApplicationConstants.ERROR_SAVING_PLAYER);
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<List<PlayerDTO>> getAllDetails() {
        PlayerDTO playerDTO = new PlayerDTO();
        try {
            List<Player> allPlayers = playersRepository.findAll();
            if (!CollectionUtils.isEmpty(allPlayers)) {
                List<PlayerDTO> playerDTOList = allPlayers.stream().map(player -> convertion.convertToDto(player, PlayerDTO.class)).toList();
                return new ResponseEntity<>(playerDTOList, HttpStatus.OK);
            }
            else {
                playerDTO.setMessage(ApplicationConstants.PLAYER_NOT_FOUND);
                return new ResponseEntity<>(List.of(playerDTO), HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            playerDTO.setMessage(ApplicationConstants.ERROR_FETCHING_PLAYERS);
            return new ResponseEntity<>(List.of(playerDTO), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<PlayerDTO> getPlayerById(UUID playerId) {
        PlayerDTO playerDTO = new PlayerDTO();
        try {
            if (!ObjectUtils.isEmpty(playerId)) {
                Player player = playersRepository.findById(playerId).orElseThrow(() -> new RuntimeException(ApplicationConstants.PLAYER_NOT_FOUND));
                if (!ObjectUtils.isEmpty(player)) {
                    return new ResponseEntity<>(convertion.convertToDto(player, PlayerDTO.class), HttpStatus.OK);
                }
            }
            playerDTO.setMessage(ApplicationConstants.NULL_INPUT);
            return new ResponseEntity<>(playerDTO, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            playerDTO.setMessage(e.getLocalizedMessage());
            return new ResponseEntity<>(playerDTO, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<BaseResponseDTO> updatePlayer( PlayerDTO playerDTO) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            if (!ObjectUtils.isEmpty(playerDTO.getId()) && !ObjectUtils.isEmpty(playerDTO)) {
                Player player = playersRepository.findById(playerDTO.getId()).orElseThrow(() -> new IllegalArgumentException(ApplicationConstants.PLAYER_NOT_FOUND));
                if (!ObjectUtils.isEmpty(player)) {
                    Player updatedPlayer = objectMapper.readerForUpdating(player).readValue(objectMapper.writeValueAsBytes(playerDTO));
                    playersRepository.save(updatedPlayer);
                    responseDTO.setMessage(ApplicationConstants.PLAYER_UPDATED_SUCCESS);
                    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
                }
            }
            responseDTO.setMessage(ApplicationConstants.NULL_INPUT);
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            responseDTO.setMessage(e.getLocalizedMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<List<MatchStatisticsDTO>> getPlayerMatchStatistics(UUID playerId) {
        MatchStatisticsDTO responseMatchStatisticsDTO = new MatchStatisticsDTO();
        try {
            if (!ObjectUtils.isEmpty(playerId)) {
                Player player = playersRepository.findById(playerId).orElseThrow(() -> new RuntimeException(ApplicationConstants.PLAYER_NOT_FOUND));
                if (!ObjectUtils.isEmpty(player)) {
                    List<MatchStatistics> matchStatisticsList =  player.getMatchStatistics();
                    List<MatchStatisticsDTO> matchStatisticsDTOList =  matchStatisticsList.stream().map(matchStatistics -> convertion.convertToDto(matchStatistics,MatchStatisticsDTO.class)).toList();
                    return new ResponseEntity<>(matchStatisticsDTOList, HttpStatus.OK);
                }
                else {
                    responseMatchStatisticsDTO.setMessage(ApplicationConstants.EMPTY_OUTPUT);
                    return new ResponseEntity<>(List.of(responseMatchStatisticsDTO), HttpStatus.NO_CONTENT);
                }
            }
            responseMatchStatisticsDTO.setMessage(ApplicationConstants.NULL_INPUT);
            return new ResponseEntity<>(List.of(responseMatchStatisticsDTO), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            responseMatchStatisticsDTO.setMessage(e.getLocalizedMessage());
            return new ResponseEntity<>(List.of(responseMatchStatisticsDTO), HttpStatus.BAD_REQUEST);
        }
    }



}

