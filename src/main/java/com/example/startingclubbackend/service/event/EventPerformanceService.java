package com.example.startingclubbackend.service.event;

import com.example.startingclubbackend.model.event.EventPerformance;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface EventPerformanceService {
    ResponseEntity<Object> assignNoteEventToAthlete(Long athleteId, Long eventId, @NotNull double note);

    void saveAthleteEvent(@NotNull EventPerformance eventPerformance) ;

    ResponseEntity<Object> fetchAllAthleteNoteByEventId(Long eventId);

    ResponseEntity<Object> fetchAllNotesByAthleteId( Long athleteId) ;

    ResponseEntity<Object> updateNoteEventOfAthlete(Long athleteId, Long eventId, @NotNull  double note);

    ResponseEntity<Object> deleteEventPerformance(Long athleteId, Long eventId);
}
