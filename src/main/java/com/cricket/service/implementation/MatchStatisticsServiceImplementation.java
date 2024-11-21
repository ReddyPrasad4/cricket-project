package com.cricket.service.implementation;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.MatchStatisticsDTO;
import com.cricket.dto.PlayerDTO;
import com.cricket.dto.TeamDTO;
import com.cricket.dto.TeamScoreDTO;
import com.cricket.entity.MatchStatistics;
import com.cricket.entity.Matches;
import com.cricket.entity.Player;
import com.cricket.entity.Team;
import com.cricket.repository.MatchRepository;
import com.cricket.repository.MatchStatisticsRepository;
import com.cricket.repository.PlayerRepository;
import com.cricket.service.intrface.MatchStatisticsService;
import com.cricket.util.ApplicationConstants;
import com.cricket.util.Convertion;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class MatchStatisticsServiceImplementation implements MatchStatisticsService {

    @Autowired
    private MatchStatisticsRepository matchStatisticsRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private Convertion convertion;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public ResponseEntity<BaseResponseDTO> saveStatistics(MatchStatisticsDTO matchStatisticsDTO) {
        BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
        try {
            if (!ObjectUtils.isEmpty(matchStatisticsDTO)) {
                MatchStatistics matchStatistics = convertion.convertToEntity(matchStatisticsDTO, MatchStatistics.class);
                if(matchStatistics.getRunsScored()==0 ||matchStatistics.getBallsFaced()==0)
                {
                    matchStatistics.setStrikeRate(0.0);
                }
                else {
                    matchStatistics.setStrikeRate((double) (matchStatistics.getRunsScored() /matchStatistics.getBallsFaced()));

                }
                if(matchStatistics.getRunsConcede()==0 || matchStatistics.getBallsBowled()==0)
                {
                    matchStatistics.setEconomy(0.0);
                }
                else {
                    matchStatistics.setEconomy(matchStatistics.getRunsConcede() /((double) matchStatistics.getBallsBowled() /6));
                }
                if(matchStatistics.getRunsConcede()==0 || matchStatistics.getWicketsTaken()==0)
                {
                    matchStatistics.setBowlingAverage(0.0);
                }
                else {
                    matchStatistics.setBowlingAverage((double) (matchStatistics.getRunsConcede() /matchStatistics.getWicketsTaken()));
                }
                if(matchStatistics.getRunsScored()==0 || matchStatistics.getNumberOfInnings()==0)
                {
                    matchStatistics.setBattingAverage(0.0);
                }
                else {
                    matchStatistics.setBattingAverage((double) matchStatistics.getRunsScored() / matchStatistics.getNumberOfInnings());

                }
                matchStatisticsRepository.save(matchStatistics);
                baseResponseDTO.setMessage(ApplicationConstants.MATCH_STATISTICS_SAVED_SUCCESS);
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            } else {
                baseResponseDTO.setMessage(ApplicationConstants.NULL_INPUT);
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            baseResponseDTO.setMessage(ApplicationConstants.ERROR_SAVING_MATCH_STATISTICS);
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<List<MatchStatisticsDTO>> getAllStatisticsDetails() {
        MatchStatisticsDTO responseMatchStatisticsDTO = new MatchStatisticsDTO();
        try {
            List<MatchStatistics> matchStatisticsList = matchStatisticsRepository.findAll();
            if (!CollectionUtils.isEmpty(matchStatisticsList)) {
                List<MatchStatisticsDTO> matchStatisticsDTOList = new ArrayList<>();
                for (MatchStatistics matchStatistics : matchStatisticsList) {
                    MatchStatisticsDTO matchStatisticsDTO = convertion.convertToDto(matchStatistics, MatchStatisticsDTO.class);
                    matchStatisticsDTOList.add(matchStatisticsDTO);
                }
                return new ResponseEntity<>(matchStatisticsDTOList, HttpStatus.OK);
            }
            responseMatchStatisticsDTO.setMessage(ApplicationConstants.EMPTY_OUTPUT);
            return new ResponseEntity<>(List.of(responseMatchStatisticsDTO),HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            responseMatchStatisticsDTO.setMessage(ApplicationConstants.ERROR_FETCHING_MATCH_STATISTICS);
            return new ResponseEntity<>(List.of(responseMatchStatisticsDTO),HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public MatchStatisticsDTO sumAllStatistics(List<MatchStatisticsDTO> matchStatisticsDTOList)
    {
        int runsScored = 0;
        int ballsFaced = 0;
        int numberOfInnings = 0;
        int hundreds = 0;
        int fifties = 0;
        int sixes = 0;
        int fours = 0;
        int ballsBowled = 0;
        int runsConceded = 0;
        int wicketsTaken = 0;
        for (MatchStatisticsDTO matchStatisticsDTO : matchStatisticsDTOList) {
            runsScored += matchStatisticsDTO.getRunsScored();
            ballsFaced+= matchStatisticsDTO.getBallsFaced();
            ballsBowled+=matchStatisticsDTO.getBallsBowled();
            runsConceded+=matchStatisticsDTO.getRunsConcede();
            wicketsTaken+=matchStatisticsDTO.getWicketsTaken();
            hundreds += matchStatisticsDTO.getHundreds();
            fifties += matchStatisticsDTO.getFifties();
            sixes += matchStatisticsDTO.getSixes();
            fours += matchStatisticsDTO.getFours();
            numberOfInnings+=matchStatisticsDTO.getNumberOfInnings();
        }
        double strikeRate = ((double) runsScored /ballsFaced )*100;
        MatchStatisticsDTO matchStatisticsDTO = new MatchStatisticsDTO();
        matchStatisticsDTO.setNumberOfInnings(numberOfInnings);
        matchStatisticsDTO.setRunsScored(runsScored);
        matchStatisticsDTO.setStrikeRate(strikeRate);
        if(runsConceded==0||ballsBowled==0)
        {
            matchStatisticsDTO.setEconomy(0.0);
        }
        else {
            matchStatisticsDTO.setEconomy(runsConceded /((double) ballsBowled /6));
        }
        matchStatisticsDTO.setBattingAverage((double) runsScored / numberOfInnings);
        matchStatisticsDTO.setBallsBowled(ballsBowled);
        matchStatisticsDTO.setRunsConcede(runsConceded);
        matchStatisticsDTO.setWicketsTaken(wicketsTaken);
        if(runsConceded==0||wicketsTaken==0)
        {
            matchStatisticsDTO.setBowlingAverage(0.0);
        }
        else {
            matchStatisticsDTO.setBowlingAverage((double) runsConceded /wicketsTaken);
        }
        matchStatisticsDTO.setHundreds(hundreds);
        matchStatisticsDTO.setFifties(fifties);
        matchStatisticsDTO.setSixes(sixes);
        matchStatisticsDTO.setFours(fours);
        return matchStatisticsDTO;
    }


    @Override
    public ResponseEntity<List<MatchStatisticsDTO>> getEveryStatisticsByPlayerId(UUID playerId) {
        MatchStatisticsDTO responseMatchStatisticsDTO = new MatchStatisticsDTO();
        try {
            if (!ObjectUtils.isEmpty(playerId)) {
                List<MatchStatistics> matchStatisticsList = matchStatisticsRepository.findByPlayerId(playerId);
                List<MatchStatisticsDTO> matchStatisticsDTOList = matchStatisticsList.stream().map(matchStatistics -> convertion.convertToDto(matchStatistics, MatchStatisticsDTO.class)).toList();
                if (!CollectionUtils.isEmpty(matchStatisticsDTOList)) {
                    return new ResponseEntity<>(matchStatisticsDTOList, HttpStatus.OK);
                }
                responseMatchStatisticsDTO.setMessage(ApplicationConstants.EMPTY_OUTPUT);
                return new ResponseEntity<>(List.of(responseMatchStatisticsDTO), HttpStatus.NO_CONTENT);
            }
            responseMatchStatisticsDTO.setMessage(ApplicationConstants.NULL_INPUT);
            return new ResponseEntity<>(List.of(responseMatchStatisticsDTO), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            responseMatchStatisticsDTO.setMessage(ApplicationConstants.ERROR_FETCHING_MATCH_STATISTICS);
            return new ResponseEntity<>(List.of(responseMatchStatisticsDTO), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *
     * @param playerId
     * @return sum of each match statistics and gives overall series statistics
     */

    @Override
    public ResponseEntity<MatchStatisticsDTO> getOverAllStatisticsOfAPlayer(UUID playerId) {
        MatchStatisticsDTO responseMatchStatisticsDTO = new MatchStatisticsDTO();
        try {
            if (!ObjectUtils.isEmpty(playerId)) {
                List<MatchStatistics> matchStatisticsList = matchStatisticsRepository.findByPlayerId(playerId);
                List<MatchStatisticsDTO> matchStatisticsDTOList = matchStatisticsList.stream().map(matchStatistics -> convertion.convertToDto(matchStatistics, MatchStatisticsDTO.class)).toList();
                if (!CollectionUtils.isEmpty(matchStatisticsDTOList)) {
                    MatchStatisticsDTO totalStatistics =  sumAllStatistics(matchStatisticsDTOList);
                    return new ResponseEntity<>(totalStatistics, HttpStatus.OK);
                }
                responseMatchStatisticsDTO.setMessage(ApplicationConstants.EMPTY_OUTPUT);
                return new ResponseEntity<>(responseMatchStatisticsDTO, HttpStatus.NO_CONTENT);
            }
            else {
                responseMatchStatisticsDTO.setMessage(ApplicationConstants.NULL_INPUT);
                return new ResponseEntity<>(responseMatchStatisticsDTO,HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            responseMatchStatisticsDTO.setMessage(ApplicationConstants.ERROR_FETCHING_MATCH_STATISTICS);
            return new ResponseEntity<>(responseMatchStatisticsDTO, HttpStatus.BAD_REQUEST);
        }

    }



    @Override
    public ResponseEntity<BaseResponseDTO> updateStatistics( MatchStatisticsDTO matchStatisticsDTO) {
        BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
        try {
            if (!ObjectUtils.isEmpty(matchStatisticsDTO.getId()) && !ObjectUtils.isEmpty(matchStatisticsDTO)) {
                Optional<MatchStatistics> matchStatisticsOptional = matchStatisticsRepository.findById(matchStatisticsDTO.getId());
                if (matchStatisticsOptional.isPresent()) {
                    MatchStatistics matchStatistics = matchStatisticsOptional.get();
                    MatchStatistics updatedMatchStatistics = objectMapper.readerForUpdating(matchStatistics).readValue(objectMapper.writeValueAsBytes(matchStatisticsDTO));
                    matchStatisticsRepository.save(updatedMatchStatistics);
                    baseResponseDTO.setMessage(ApplicationConstants.MATCH_STATISTICS_UPDATED_SUCCESS);
                    return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
                } else {
                    baseResponseDTO.setMessage(ApplicationConstants.EMPTY_OUTPUT);
                    return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
                }
            }
            else {
                baseResponseDTO.setMessage(ApplicationConstants.NULL_INPUT);
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            baseResponseDTO.setMessage(ApplicationConstants.ERROR_UPDATING_MATCH_STATISTICS + e.getMessage());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<MatchStatisticsDTO> getPlayerSeriesStatistics(UUID playerId,UUID seriesId)
    {
        List<Matches> matchesList = matchRepository.findBySeriesId(seriesId);
        if(!CollectionUtils.isEmpty(matchesList))
        {
            List<MatchStatisticsDTO> seriesStatisticsList = new ArrayList<>();
            matchesList.forEach( matches -> {
                MatchStatistics matchStatisticsList = matchStatisticsRepository.findByPlayerIdAndMatchesId(playerId,matches.getId());
                if (!ObjectUtils.isEmpty(matchStatisticsList))
                {
                    seriesStatisticsList.add(convertion.convertToDto(matchStatisticsList,MatchStatisticsDTO.class));
                }
            });
            return seriesStatisticsList;
        }
        return Collections.emptyList();
    }

    @Override
    public ResponseEntity<List<MatchStatisticsDTO>> getSeriesStatisticsOfAPlayer(UUID playerId, UUID seriesId) {
        MatchStatisticsDTO responseMatchStatisticsDTO = new MatchStatisticsDTO();
        try
        {
            if (!ObjectUtils.isEmpty(playerId) && !ObjectUtils.isEmpty(seriesId))
            {
                List<MatchStatisticsDTO> playerSeriesStatistics = getPlayerSeriesStatistics(playerId, seriesId);
                if (!CollectionUtils.isEmpty(playerSeriesStatistics))
                {
                    return new ResponseEntity<>(playerSeriesStatistics,HttpStatus.OK);
                }
                responseMatchStatisticsDTO.setMessage(ApplicationConstants.MATCH_STATISTICS_NOT_FOUND);
                return new ResponseEntity<>(List.of(responseMatchStatisticsDTO),HttpStatus.NO_CONTENT);
            }
            else {
                responseMatchStatisticsDTO.setMessage(ApplicationConstants.NULL_INPUT);
                return new ResponseEntity<>(List.of(responseMatchStatisticsDTO),HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            responseMatchStatisticsDTO.setMessage(ApplicationConstants.ERROR_UPDATING_MATCH_STATISTICS + e.getMessage());
            return new ResponseEntity<>(List.of(responseMatchStatisticsDTO), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<MatchStatisticsDTO> getMatchStatisticsOfAPlayer(UUID playerId , UUID matchId) {
        MatchStatisticsDTO responseMatchStatisticsDTO = new MatchStatisticsDTO();
        try
        {
            if (!ObjectUtils.isEmpty(playerId) && !ObjectUtils.isEmpty(playerId) && !ObjectUtils.isEmpty(matchId))
            {
                MatchStatistics matchStatistics = matchStatisticsRepository.findByPlayerIdAndMatchesId(matchId,playerId);
                if (!ObjectUtils.isEmpty(matchStatistics))
                {
                    MatchStatisticsDTO playerMatchStatistics = convertion.convertToDto(matchStatistics, MatchStatisticsDTO.class);
                    return new ResponseEntity<>(playerMatchStatistics,HttpStatus.OK);
                }
                responseMatchStatisticsDTO.setMessage(ApplicationConstants.MATCH_STATISTICS_NOT_FOUND);
                return new ResponseEntity<>(responseMatchStatisticsDTO,HttpStatus.NO_CONTENT);
            }
            else {
                responseMatchStatisticsDTO.setMessage(ApplicationConstants.NULL_INPUT);
                return new ResponseEntity<>(responseMatchStatisticsDTO,HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            responseMatchStatisticsDTO.setMessage(ApplicationConstants.ERROR_UPDATING_MATCH_STATISTICS + e.getMessage());
            return new ResponseEntity<>(responseMatchStatisticsDTO, HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<List<TeamDTO>> getEachTeamScore(UUID matchId) {
        TeamDTO responseTeamDTO = new TeamDTO();
        try {
            Matches matches = matchRepository.findById(matchId).orElseThrow(()-> new RuntimeException(ApplicationConstants.MATCH_NOT_FOUND));
            if(!ObjectUtils.isEmpty(matches)){
                List<Team> teamList = matches.getTeams();
                List<TeamDTO> teamDTOList= teamList.stream().map(team -> {
                    List<Player> playerList =  team.getPlayers();
                    List<PlayerDTO> playerDTOList =   playerList.stream().map(player -> {
                        MatchStatistics playerMatchStat = matchStatisticsRepository.findByPlayerIdAndMatchesId(player.getId(),matchId);
                        PlayerDTO playerDTO = convertion.convertToDto(player, PlayerDTO.class);
                        MatchStatisticsDTO matchStatisticsDTO = convertion.convertToDto(playerMatchStat,MatchStatisticsDTO.class);
                        playerDTO.setMatchStatisticsDTOList(List.of(matchStatisticsDTO));
                        return playerDTO;
                    }).toList();
                    TeamDTO teamDTO = convertion.convertToDto(team, TeamDTO.class);
                    teamDTO.setPlayerDTOList(playerDTOList);
                    return  teamDTO;
                }).toList();
                return new ResponseEntity<>(teamDTOList,HttpStatus.OK);
            }

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        responseTeamDTO.setMessage(ApplicationConstants.ERROR_FETCHING_MATCH_STATISTICS);
        return new ResponseEntity<>(List.of(responseTeamDTO), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<TeamScoreDTO>> getTeamScore(UUID matchId) {
        TeamScoreDTO teamScoreDTO = new TeamScoreDTO();
        List<TeamScoreDTO> teamScoreDTOList = new ArrayList<>();
        try {
            Matches matches = matchRepository.findById(matchId).orElseThrow(RuntimeException::new);
            if(!ObjectUtils.isEmpty(matches)){
                List<Team> team = matches.getTeams();
                for (Team fetechedTeam : team) {
                    List<Player> playerList = fetechedTeam.getPlayers();
                    int totalruns = 0;
                    for (Player player : playerList) {
                        MatchStatistics matchStatistics =  matchStatisticsRepository.findByPlayerIdAndMatchesId(player.getId(),matchId);
                        if(!ObjectUtils.isEmpty(matchStatistics))
                        {
                            totalruns+=matchStatistics.getRunsScored();
                        }
                    }
                    TeamScoreDTO teamScore = new TeamScoreDTO();
                    teamScore.setTotalScore(totalruns);
                    teamScore.setTeamName(fetechedTeam.getName());
                    teamScoreDTOList.add(teamScore);

                }
                return new ResponseEntity<>(teamScoreDTOList,HttpStatus.OK);
            }

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        teamScoreDTO.setMessage(ApplicationConstants.ERROR_FETCHING_MATCH_STATISTICS);
        return new ResponseEntity<>(List.of(teamScoreDTO), HttpStatus.BAD_REQUEST);
    }

}
