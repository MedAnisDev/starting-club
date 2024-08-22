package com.example.startingclubbackend.controller.performance;

import com.example.startingclubbackend.DTO.performance.trainingSession.TrainingSessionDTO;
import com.example.startingclubbackend.service.performance.TrainingSessionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/training_session")
public class TrainingSessionController {
    private final TrainingSessionService trainingSessionService;

    public TrainingSessionController(TrainingSessionService trainingSessionService) {
        this.trainingSessionService = trainingSessionService;
    }

    @PostMapping("/{performanceId}")
    public ResponseEntity<Object> createTrainingSession(@NotNull @Valid @RequestBody final  TrainingSessionDTO trainingSessionDTO ,
                                                        @PathVariable("performanceId") final Long performanceId) {
        return trainingSessionService.createTrainingSession(trainingSessionDTO , performanceId);
    }

    @PutMapping("/{sessionId}")
    public ResponseEntity<Object> updateTrainingSession(@PathVariable("sessionId") final Long sessionId,
                                                        @NotNull @Valid @RequestBody final TrainingSessionDTO trainingSessionDTO) {
        return trainingSessionService.updateTrainingSession(sessionId, trainingSessionDTO);
    }

    @GetMapping("/{pageNumber}")
    public ResponseEntity<Object> fetchAllTrainingSessions(@PathVariable("pageNumber") long pageNumber,
                                                           @RequestParam(value = "sortedBY", defaultValue = "id") final String sortedBY) {
        return trainingSessionService.fetchAllTrainingSessions(pageNumber, sortedBY);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Object> deleteTrainingSessionById(@PathVariable("sessionId") final Long sessionId) {
        return trainingSessionService.deleteTrainingSessionById(sessionId);
    }

    @DeleteMapping("/all/{performanceId}")
    public ResponseEntity<Object> deleteAllTrainingSessions(@PathVariable("performanceId") final Long performanceId) {
        trainingSessionService.deleteAllSessionByPerformanceId(performanceId);
        return new ResponseEntity<>("all training sessions are deleted successfully", HttpStatus.OK) ;
    }
}
