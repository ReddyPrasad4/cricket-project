package com.cricket.controller;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.MatchStatisticsDTO;
import com.cricket.dto.TeamDTO;
import com.cricket.dto.TeamScoreDTO;
import com.cricket.service.intrface.MatchStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/statistics")
public class MatchStatisticsController {

    @Autowired
    private MatchStatisticsService matchStatisticsService;

    @PostMapping("/save")
    public ResponseEntity<BaseResponseDTO> saveStatistics(@RequestBody MatchStatisticsDTO matchStatisticsDTO) {
        return matchStatisticsService.saveStatistics(matchStatisticsDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MatchStatisticsDTO>> getAllStatisticsDetails() {
        return matchStatisticsService.getAllStatisticsDetails();
    }
    @GetMapping("/get-match-statics/{matchId}/{playerId}")
    public ResponseEntity<MatchStatisticsDTO> getMatchStatisticsOfAPlayer(@PathVariable UUID matchId,@PathVariable UUID playerId) {
        return matchStatisticsService.getMatchStatisticsOfAPlayer(matchId,playerId);
    }

    @GetMapping("/get-total-statics/{playerId}")
    public ResponseEntity<MatchStatisticsDTO> getStatisticsById(@PathVariable UUID playerId) {
        return matchStatisticsService.getOverAllStatisticsOfAPlayer(playerId);
    }
    @GetMapping("/get-every-statistics-by-player-id/{playerId}")
    public ResponseEntity<List<MatchStatisticsDTO>> getAllStatisticsOfAPlayer(@PathVariable UUID playerId) {
        return matchStatisticsService.getEveryStatisticsByPlayerId(playerId);
    }

    @GetMapping("/get-series-statistics-of-player")
    public ResponseEntity<List<MatchStatisticsDTO>> getAllStatisticsOfAPlayer(@RequestParam UUID playerId,@RequestParam UUID seriesId) {
        return matchStatisticsService.getSeriesStatisticsOfAPlayer(playerId,seriesId);
    }


    @PutMapping("/update")
    public ResponseEntity<BaseResponseDTO> updateStatistics( @RequestBody MatchStatisticsDTO matchStatisticsDTO) {
        return matchStatisticsService.updateStatistics( matchStatisticsDTO);
    }

    @GetMapping("/get-team-Score-by-match-id/{matchId}")
    public ResponseEntity<List<TeamDTO>> getTeamScore(@PathVariable UUID matchId){
        return matchStatisticsService.getEachTeamScore(matchId);
    }

    @GetMapping("/get-team-total-Score-by-match-id/{matchId}")
    public ResponseEntity<List<TeamScoreDTO>> getTeamTotalScore(@PathVariable UUID matchId){
        return matchStatisticsService.getTeamScore(matchId);
    }

}

