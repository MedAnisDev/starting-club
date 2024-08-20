package com.example.startingclubbackend.service.event;

import com.example.startingclubbackend.model.event.AthleteEvent;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface AthleteEventService {
    ResponseEntity<Object> assignNoteEventToAthlete(Long athleteId, Long eventId, @NotNull String note);

    void saveAthleteEvent(@NotNull AthleteEvent athleteEvent) ;

    ResponseEntity<Object> fetchAllAthleteNoteByEventId(Long eventId);

    public ResponseEntity<Object> fetchAllNotesByAthleteId( Long athleteId) ;

    ResponseEntity<Object> updateNoteEventOfAthlete(Long athleteId, Long eventId, @NotNull  String note);

    ResponseEntity<Object> deleteAthleteEvent(Long athleteId, Long eventId);
}
