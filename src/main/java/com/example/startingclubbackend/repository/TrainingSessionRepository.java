package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.performance.TrainingSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface TrainingSessionRepository extends JpaRepository<TrainingSession , Long> {
    @Query("SELECT ts FROM TrainingSession ts WHERE ts.id = :sessionId")
    Optional<TrainingSession> fetchById(@Param("sessionId") Long sessionId);

    @Query("SELECT ts FROM TrainingSession ts")
    List<TrainingSession> fetchAllTrainingSessions(Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM TrainingSession ts WHERE ts.id = :sessionId ")
    void deleteTrainingSessionById(@Param("sessionId") Long sessionId);

    @Modifying
    @Transactional
    @Query("DELETE from TrainingSession ts where ts.performance.id = :performanceId")
    void deleteAllByPerformanceId(@Param("performanceId") Long performanceId);
}
