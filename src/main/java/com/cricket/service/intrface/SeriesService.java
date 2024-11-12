package com.cricket.service.intrface;

import com.cricket.dto.BaseResponseDTO;
import com.cricket.dto.SeriesDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface SeriesService {
     ResponseEntity<BaseResponseDTO> saveSeries(SeriesDTO seriesDTO);
     ResponseEntity<List<SeriesDTO>> getAllSeries();
     ResponseEntity<SeriesDTO> getSeries(UUID seriesId);
     ResponseEntity<BaseResponseDTO> updateSeries(SeriesDTO seriesDTO);

}