package com.cricket.controller;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.SeriesDTO;
import com.cricket.service.intrface.SeriesService;
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
@RequestMapping("/api/series")
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @PostMapping("/save-series")
    public ResponseEntity<BaseResponseDTO> addSeries(@RequestBody SeriesDTO seriesDTO) {
        return seriesService.saveSeries(seriesDTO);
    }

    @GetMapping("/get-all-series")
    public ResponseEntity<List<SeriesDTO>> getAllSeries() {
        return seriesService.getAllSeries();
    }

    @GetMapping("/get-series-by-id/{id}")
    public ResponseEntity<SeriesDTO> getSeriesById(@PathVariable("id") UUID seriesId) {
        return seriesService.getSeries(seriesId);
    }


    @PutMapping("/update-series")
    public ResponseEntity<BaseResponseDTO> updateSeries( @RequestBody SeriesDTO seriesDTO) {
        return seriesService.updateSeries( seriesDTO);
    }
}