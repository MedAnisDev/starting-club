package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.event.EventPerformance;
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
public interface EventPerformanceRepository extends JpaRepository<EventPerformance, Long> {

    @Query("SELECT a FROM EventPerformance a where a.event.id = :eventId")
    List<EventPerformance> fetchAllAthleteNoteByEventId(@Param("eventId") Long eventId);

    @Query("SELECT a FROM EventPerformance a where a.athlete.id = :athleteId")
    List<EventPerformance>fetchAllNotesByAthleteId(@Param("athleteId") Long athleteId) ;

    @Query("SELECT a FROM EventPerformance a where a.athlete.id = :athleteId AND a.event.id = :eventId")
    Optional<EventPerformance> fetchWithAthleteIdAndEventId(@Param("athleteId") Long athleteId, @Param("eventId") Long eventId);

    @Transactional
    @Modifying
    @Query("DELETE FROM EventPerformance a where a.athlete.id = :athleteId AND a.event.id = :eventId ")
    void deleteAthleteEvent(@Param("athleteId") Long athleteId, @Param("eventId") Long eventId);
}
