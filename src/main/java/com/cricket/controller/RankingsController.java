package com.cricket.controller;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.RankingsDTO;
import com.cricket.service.intrface.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rankings")
public class RankingsController {

    @Autowired
    private RankingService rankingService;

    @GetMapping("/get-all-rankings")
    public ResponseEntity<List<RankingsDTO>> getAllRankings()
    {
        return rankingService.getAllRankings();
    }

    @PostMapping("/assign-rankings/{seriesId}")
    public ResponseEntity<BaseResponseDTO> assignRankings(@PathVariable UUID seriesId)
    {
        return rankingService.assignRankings(seriesId);
    }
}
