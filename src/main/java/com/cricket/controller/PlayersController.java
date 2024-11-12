package com.cricket.controller;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.MatchStatisticsDTO;
import com.cricket.dto.PlayerDTO;
import com.cricket.service.intrface.PlayerService;
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
@RequestMapping("/api/players")
public class PlayersController {

    @Autowired
    private PlayerService playerService;
    @PostMapping("/save-player")
    public ResponseEntity<BaseResponseDTO> addPlayer(@RequestBody PlayerDTO playerDTO){
        return playerService.savePlayer(playerDTO);
    }
    @GetMapping("/get-all-players")
    public ResponseEntity<List<PlayerDTO>> fetchAllPlayers(){
        return playerService.getAllDetails();
    }
    @GetMapping("/get-player-by-id/{playerId}")
    public ResponseEntity<PlayerDTO> fetchPlayerById(@PathVariable UUID playerId){
        return playerService.getPlayerById(playerId);
    }

    @PutMapping("/update-player")
    public ResponseEntity<BaseResponseDTO> updatePlayerData(@RequestBody PlayerDTO playerDTO){
        return playerService.updatePlayer(playerDTO);
    }

    @GetMapping("/get-player-statistics-id/{playerId}")
    public ResponseEntity<List<MatchStatisticsDTO>> fetchPlayerByMatchStatistics(@PathVariable UUID playerId){
        return playerService.getPlayerMatchStatistics(playerId);
    }





}
