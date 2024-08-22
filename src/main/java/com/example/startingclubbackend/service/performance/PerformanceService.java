package com.example.startingclubbackend.service.performance;

import com.example.startingclubbackend.DTO.performance.PerformanceDTO;
import com.example.startingclubbackend.model.performance.Performance;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface PerformanceService {
    ResponseEntity<Object> createPerformance(@NotNull PerformanceDTO performanceDTO , Long athleteId);
    Performance getPerformanceById(Long performanceId);

    ResponseEntity<Object> updatePerformance(@NotNull PerformanceDTO performanceDTO , Long performanceId);

    ResponseEntity<Object> getPerformanceByAthleteId(Long athleteId);

    ResponseEntity<Object> deletePerformanceById(Long performanceId);
}
