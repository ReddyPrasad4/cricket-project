package com.cricket.service.implementation;


import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.MatchStatisticsDTO;
import com.cricket.dto.PlayerDTO;
import com.cricket.dto.RankingsDTO;
import com.cricket.entity.Matches;
import com.cricket.entity.Player;
import com.cricket.entity.Rankings;
import com.cricket.entity.Team;
import com.cricket.repository.MatchRepository;
import com.cricket.repository.RankingsRepository;
import com.cricket.service.intrface.MatchStatisticsService;
import com.cricket.service.intrface.RankingService;
import com.cricket.util.ApplicationConstants;
import com.cricket.util.Convertion;
import com.cricket.util.PlayerRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class RankingServiceImplementation implements RankingService {


    @Autowired
    private RankingsRepository rankingsRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchStatisticsService matchStatisticsService;

    @Autowired
    private Convertion convertion;

    public ResponseEntity<BaseResponseDTO> assignRankings(UUID seriesId) {
        BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
        try {
            List<Matches> listOfMatches = matchRepository.findBySeriesId(seriesId);
            if (CollectionUtils.isEmpty(listOfMatches))
            {
                baseResponseDTO.setMessage(ApplicationConstants.MATCH_NOT_FOUND);
                return new ResponseEntity<>(baseResponseDTO,HttpStatus.BAD_REQUEST);
            }
            List<Team> teams = listOfMatches.get(0).getTeams();
            List<Player> playersList = new ArrayList<>();
            teams.forEach(team -> playersList.addAll(team.getPlayers()));

            Map<Player, Double> playerScores = new HashMap<>();
            for (Player player : playersList) {
                List<MatchStatisticsDTO> playerSeriesStatistics = matchStatisticsService.getPlayerSeriesStatistics(player.getId(), seriesId);
                MatchStatisticsDTO matchStatisticsDTO = matchStatisticsService.sumAllStatistics(playerSeriesStatistics);
                double performanceScore = calculatePerformanceScore(player, matchStatisticsDTO);
                playerScores.put(player, performanceScore);
            }

            playersList.sort((player1, player2) -> Double.compare(playerScores.get(player2), playerScores.get(player1)));

            int rank = 1;
            for (Player player : playersList) {
                Rankings ranking = new Rankings();
                ranking.setPlayer(player);
                ranking.setRanking(rank++);
                rankingsRepository.save(ranking);
            }

            baseResponseDTO.setMessage(ApplicationConstants.RANKING_SAVED_SUCCESS);
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error(ApplicationConstants.ERROR_SAVING_RANKING, e);
            baseResponseDTO.setMessage(ApplicationConstants.ERROR_SAVING_RANKING);
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    public double calculatePerformanceScore(Player player, MatchStatisticsDTO stats) {
        try {
            if (!ObjectUtils.isEmpty(player)) {
                double performanceScore = 0;
                if (PlayerRole.BATSMAN.toString().equals(player.getRole())) {
                    performanceScore = calculateBatsmanScore(stats);
                } else if (PlayerRole.BOWLER.toString().equals(player.getRole())) {
                    performanceScore = calculateBowlerScore(stats);
                } else if (PlayerRole.ALLROUNDER.toString().equals(player.getRole())) {
                    performanceScore = calculateBatsmanScore(stats) + calculateBowlerScore(stats);
                }
                return performanceScore;
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return 0.0;
    }

    public double calculateBatsmanScore(MatchStatisticsDTO stats) {
        return (stats.getRunsScored() * 0.4) +
                (stats.getStrikeRate() * 0.2) +
                (stats.getBattingAverage() * 0.3) +
                (stats.getHundreds() * 10) +
                (stats.getFifties() * 5);
    }

    public double calculateBowlerScore(MatchStatisticsDTO stats) {
        return (stats.getEconomy() != 0 ? 10 / stats.getEconomy() : 0) +
                (stats.getNumberOfInnings() * 0.1);
    }


    @Override
    public ResponseEntity<List<RankingsDTO>> getAllRankings()
    {
        RankingsDTO responseRankingsDTO = new RankingsDTO();
        try {
            List<Rankings> rankingsList =  rankingsRepository.getAllRankingsInOrder();
            if(!CollectionUtils.isEmpty(rankingsList))
            {
                List<RankingsDTO> rankingsDTOList = rankingsList.stream().map(rankings -> {
                    RankingsDTO rankingsDTO = convertion.convertToDto(rankings, RankingsDTO.class);
                    rankingsDTO.setPlayerDTO(convertion.convertToDto(rankings.getPlayer(), PlayerDTO.class));
                    return rankingsDTO;
                }).toList();
                return new ResponseEntity<>(rankingsDTOList, HttpStatus.OK);
            }
            responseRankingsDTO.setMessage(ApplicationConstants.RANKING_NOT_FOUND);
            return new ResponseEntity<>(List.of(responseRankingsDTO), HttpStatus.NO_CONTENT);

        }
        catch (Exception e)
        {
            log.error(e.getLocalizedMessage());
            responseRankingsDTO.setMessage(ApplicationConstants.ERROR_FETCHING_RANKINGS);
            return new ResponseEntity<>(List.of(responseRankingsDTO), HttpStatus.BAD_REQUEST);
        }

    }
}
