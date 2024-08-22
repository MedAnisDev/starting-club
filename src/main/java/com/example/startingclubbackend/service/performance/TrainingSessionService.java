package com.example.startingclubbackend.service.performance;

import com.example.startingclubbackend.DTO.performance.trainingSession.TrainingSessionDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface TrainingSessionService {

    ResponseEntity<Object> createTrainingSession(@NotNull TrainingSessionDTO trainingSessionDTO ,Long performanceId);

    ResponseEntity<Object> updateTrainingSession(Long sessionId, @NotNull TrainingSessionDTO trainingSessionDTO);

    ResponseEntity<Object> fetchAllTrainingSessions(long pageNumber, String sortedBY);

    ResponseEntity<Object> deleteTrainingSessionById(Long sessionId);
    void deleteAllSessionByPerformanceId(Long performanceId) ;
}
