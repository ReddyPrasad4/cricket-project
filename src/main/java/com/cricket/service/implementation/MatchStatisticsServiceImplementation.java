package com.cricket.service.implementation;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.MatchStatisticsDTO;
import com.cricket.entity.MatchStatistics;
import com.cricket.entity.Matches;
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
                matchStatisticsRepository.save(matchStatistics);
                baseResponseDTO.setMessage(ApplicationConstants.MATCH_STATISTICS_SAVED_SUCCESS);
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
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
        matchStatisticsDTO.setEconomy(runsConceded /((double) ballsBowled /6));
        matchStatisticsDTO.setBattingAverage((double) runsScored / numberOfInnings);
        matchStatisticsDTO.setBallsBowled(ballsBowled);
        matchStatisticsDTO.setRunsConcede(runsConceded);
        matchStatisticsDTO.setWicketsTaken(wicketsTaken);
        matchStatisticsDTO.setBowlingAverage((double) runsConceded /wicketsTaken);
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
                seriesStatisticsList.add(convertion.convertToDto(matchStatisticsList,MatchStatisticsDTO.class));
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
            return new ResponseEntity<>(List.of(responseMatchStatisticsDTO), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
