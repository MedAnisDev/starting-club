package com.example.startingclubbackend.controller.performance;

import com.example.startingclubbackend.DTO.performance.PerformanceDTO;
import com.example.startingclubbackend.service.performance.PerformanceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/performance")
public class PerformanceController {
    private final PerformanceService performanceService;


    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @PostMapping("/{athleteId}")
    public ResponseEntity<Object> createPerformance(@PathVariable("athleteId") final Long athleteId,
                                                    @NotNull @Valid @RequestBody final PerformanceDTO performanceDTO) {
        return performanceService.createPerformance(performanceDTO, athleteId);
    }

    @PutMapping("/{performanceId}")
    public ResponseEntity<Object> updatePerformance(@NotNull @Valid @RequestBody final PerformanceDTO performanceDTO,
                                                    @PathVariable("performanceId") final Long performanceId) {
        return performanceService.updatePerformance(performanceDTO ,performanceId);
    }

    @GetMapping("/{athleteId}")
    public ResponseEntity<Object> getPerformanceByAthleteId(@PathVariable("athleteId") final Long athleteId) {
        return performanceService.getPerformanceByAthleteId(athleteId);
    }

    @DeleteMapping("/{performanceId}")
    public ResponseEntity<Object> deletePerformanceById(@PathVariable("performanceId") final Long performanceId) {
        return performanceService.deletePerformanceById(performanceId);
    }



}
