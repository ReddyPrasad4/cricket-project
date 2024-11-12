package com.cricket.service.implementation;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.MatchStatisticsDTO;
import com.cricket.dto.MatchesDTO;
import com.cricket.dto.PlayerDTO;
import com.cricket.dto.TeamDTO;
import com.cricket.entity.MatchStatistics;
import com.cricket.entity.Matches;
import com.cricket.entity.Team;
import com.cricket.repository.MatchRepository;
import com.cricket.service.intrface.MatchesService;
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
public class MatchesServiceImplementation implements MatchesService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Convertion convertion;

    @Override
    public ResponseEntity<BaseResponseDTO> saveMatches(MatchesDTO matchesDTO) {
        BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
        try {
            if (!ObjectUtils.isEmpty(matchesDTO) && !StringUtils.isEmpty(matchesDTO.getVenue())) {
                matchRepository.save(convertion.convertToEntity(matchesDTO,Matches.class));
                baseResponseDTO.setMessage(ApplicationConstants.MATCH_SAVED_SUCCESS);
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
            }
            else {
                baseResponseDTO.setMessage(ApplicationConstants.NULL_INPUT);
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            baseResponseDTO.setMessage(ApplicationConstants.ERROR_SAVING_MATCH);
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<List<MatchesDTO>> getAllMatches() {
        MatchesDTO matchesDTO = new MatchesDTO();
        try {
            List<Matches> matchesList = matchRepository.findAll();
            if (!CollectionUtils.isEmpty(matchesList)) {
                List<MatchesDTO> matchesDTOList = matchesList.stream().map(matches ->  convertion.convertToDto(matches, MatchesDTO.class)).toList();
                return new ResponseEntity<>(matchesDTOList, HttpStatus.OK);
            }
            else {
                matchesDTO.setMessage(ApplicationConstants.MATCH_NOT_FOUND);
                return new ResponseEntity<>(List.of(matchesDTO), HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            matchesDTO.setMessage(ApplicationConstants.ERROR_FETCHING_MATCHES);
            return new ResponseEntity<>(List.of(matchesDTO), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<MatchesDTO> getMatchesById(UUID matchId) {
        MatchesDTO matchesDTO = new MatchesDTO();
        try {
            if (!ObjectUtils.isEmpty(matchId))
            {
                Matches matches = matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException(ApplicationConstants.MATCH_NOT_FOUND));
                MatchesDTO retrievedMatchesDTO = convertion.convertToDto(matches, MatchesDTO.class);
                return new ResponseEntity<>(retrievedMatchesDTO, HttpStatus.OK);
            }
            else {
                matchesDTO.setMessage(ApplicationConstants.NULL_INPUT);
                return new ResponseEntity<>(matchesDTO,HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            matchesDTO.setMessage(e.getLocalizedMessage());
            return new ResponseEntity<>(matchesDTO, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<BaseResponseDTO> updateMatches(MatchesDTO matchesDTO) {
        BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
        try {
            Matches existingMatch = matchRepository.findById(matchesDTO.getId()).orElseThrow(() -> new RuntimeException(ApplicationConstants.MATCH_NOT_FOUND));

            Matches updatedMatches = objectMapper.readerForUpdating(existingMatch).readValue(objectMapper.writeValueAsBytes(matchesDTO));

            matchRepository.save(updatedMatches);
            baseResponseDTO.setMessage(ApplicationConstants.MATCH_UPDATED_SUCCESS);
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            baseResponseDTO.setMessage(ApplicationConstants.ERROR_UPDATING_MATCH);
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *
     * @param matchId
     * @return <List<TeamDTO>
     *
     * Finding matches based on matchId
     */

    @Override
    public ResponseEntity<List<TeamDTO>> getTeamsByMatchId(UUID matchId) {
        TeamDTO teamDTO = new TeamDTO();
        try
        {
            if (!ObjectUtils.isEmpty(matchId))
            {
                Matches retrievedMatch = matchRepository.findById(matchId).orElseThrow(()-> new RuntimeException(ApplicationConstants.MATCH_NOT_FOUND));
                if(!ObjectUtils.isEmpty(retrievedMatch))
                {
                    List<Team> teamList = retrievedMatch.getTeams();
                    if(!CollectionUtils.isEmpty(teamList))
                    {
                        List<TeamDTO> teamDTOList =  teamList.stream().map(team -> {
                            TeamDTO belongingTeam = convertion.convertToDto(team, TeamDTO.class);
                            belongingTeam.setPlayerDTOList(team.getPlayers().stream().map(player -> convertion.convertToDto(player, PlayerDTO.class)).toList());
                            return belongingTeam;
                        }).toList();

                        return new ResponseEntity<>(teamDTOList,HttpStatus.OK);
                    }
                    teamDTO.setMessage(ApplicationConstants.MATCH_NOT_FOUND);
                    return new ResponseEntity<>(List.of(teamDTO),HttpStatus.NO_CONTENT);
                }
            }
            teamDTO.setMessage(ApplicationConstants.NULL_INPUT);
            return new ResponseEntity<>(List.of(teamDTO),HttpStatus.BAD_REQUEST);
        }
        catch (Exception e )
        {
            log.error(e.getLocalizedMessage());
            teamDTO.setMessage(e.getLocalizedMessage());
            return new ResponseEntity<>(List.of(teamDTO),HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<List<MatchStatisticsDTO>> getMatchStatisticsByMatchId(UUID matchId) {
        MatchStatisticsDTO matchStatisticsDTO = new MatchStatisticsDTO();
        try
        {
            if (!ObjectUtils.isEmpty(matchId))
            {
                Matches retrievedMatch = matchRepository.findById(matchId).orElseThrow(()-> new RuntimeException(ApplicationConstants.MATCH_NOT_FOUND));
                if(!ObjectUtils.isEmpty(retrievedMatch))
                {
                    List<MatchStatistics> matchStatisticsList = retrievedMatch.getMatchStatistics();
                    if(!CollectionUtils.isEmpty(matchStatisticsList))
                    {
                        List<MatchStatisticsDTO> matchStatisticsDTOList =  matchStatisticsList.stream().map(matchStatistic -> convertion.convertToDto(matchStatistic, MatchStatisticsDTO.class)).toList();
                        return new ResponseEntity<>(matchStatisticsDTOList,HttpStatus.OK);
                    }
                }
                matchStatisticsDTO.setMessage(ApplicationConstants.MATCH_NOT_FOUND);
                return new ResponseEntity<>(List.of(matchStatisticsDTO),HttpStatus.NO_CONTENT);
            }
            matchStatisticsDTO.setMessage(ApplicationConstants.NULL_INPUT);
            return new ResponseEntity<>(List.of(matchStatisticsDTO),HttpStatus.BAD_REQUEST);
        }
        catch (Exception e )
        {
            log.error(e.getLocalizedMessage());
            matchStatisticsDTO.setMessage(e.getLocalizedMessage());
            return new ResponseEntity<>(List.of(matchStatisticsDTO),HttpStatus.BAD_REQUEST);
        }
    }
}