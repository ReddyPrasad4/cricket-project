package com.cricket.controller;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.MatchStatisticsDTO;
import com.cricket.dto.MatchesDTO;
import com.cricket.dto.TeamDTO;
import com.cricket.service.intrface.MatchesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchesService matchesService;


    @PostMapping("/save-match")
    public ResponseEntity<BaseResponseDTO> addMatch(@RequestBody MatchesDTO matchesDTO) {
        return matchesService.saveMatches(matchesDTO);
    }


    @GetMapping("/get-all-matches")
    public ResponseEntity<List<MatchesDTO>> fetchAllMatches() {
        return matchesService.getAllMatches();
    }


    @GetMapping("/get-matches-by-id/{matchId}")
    public ResponseEntity<MatchesDTO> fetchMatchById(@PathVariable UUID matchId) {
        return matchesService.getMatchesById(matchId);
    }


    @PutMapping("/update-matches")
    public ResponseEntity<BaseResponseDTO> updateMatch( @RequestBody MatchesDTO matchesDTO) {
        return matchesService.updateMatches(matchesDTO);
    }


    @GetMapping("/get-teams/{matchId}")
    public ResponseEntity<List<TeamDTO>>  getTeams(@PathVariable UUID matchId) {
        return matchesService.getTeamsByMatchId(matchId);
    }
    @GetMapping("/get-matchStatistics/{matchId}")
    public ResponseEntity<List<MatchStatisticsDTO>>  getMatchStatistics(@PathVariable UUID matchId) {
        return matchesService.getMatchStatisticsByMatchId(matchId);
    }
}