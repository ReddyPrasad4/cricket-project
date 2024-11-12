package com.cricket.controller;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.PlayerDTO;
import com.cricket.dto.TeamDTO;
import com.cricket.service.intrface.TeamService;
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
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;


    @PostMapping("/save-team/{matchId}")
    public ResponseEntity<BaseResponseDTO> createTeam(@RequestBody TeamDTO teamDTO,@PathVariable UUID matchId) {
        return teamService.createTeam(teamDTO,matchId);
    }


    @GetMapping("/get-teams")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        return teamService.getAllTeams();
    }


    @GetMapping("/get-team-by-id/{teamId}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable UUID teamId) {
        return teamService.getTeamById(teamId);
    }
    @PutMapping("/update-team")
    public ResponseEntity<BaseResponseDTO> updateTeam(@RequestBody TeamDTO teamDTO) {
        return teamService.updateTeam(teamDTO);
    }

    @GetMapping("/get-players-by-id/{teamId}")
    public ResponseEntity<List<PlayerDTO>> getPlayersByTeamId(@PathVariable UUID teamId) {
        return teamService.getPlayersByTeamId(teamId);
    }


    @GetMapping("/get-players-by-role")
    public ResponseEntity<List<PlayerDTO>> getPlayersByRole(@RequestParam UUID teamId, @RequestParam String playerRole){
        return teamService.getPlayersByRole(playerRole,teamId);
    }
}
