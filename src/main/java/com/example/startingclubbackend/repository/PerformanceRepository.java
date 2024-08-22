package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.performance.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface PerformanceRepository extends JpaRepository<Performance , Long> {

    @Query("SELECT p FROM Performance p where p.athlete.id = :athleteId")
    Optional<Performance>fetchByAthleteId(@Param("athleteId")Long athleteId) ;

    @Query("SELECT p FROM Performance p where p.id = :performanceId")
    Optional<Performance>fetchByPerformanceId(@Param("performanceId")Long performanceId) ;
}
