package com.example.startingclubbackend.controller.event;

import com.example.startingclubbackend.service.event.AthleteEventService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/athlete_event")
public class AthleteEventController {
    private final AthleteEventService athleteEventService ;

    public AthleteEventController(AthleteEventService athleteEventService) {
        this.athleteEventService = athleteEventService;
    }

    @PostMapping("/add/{athleteId}/{eventId}")
    public ResponseEntity<Object> assignNoteEventToAthlete(@PathVariable final Long athleteId,
                                                           @PathVariable final Long eventId,
                                                           @RequestParam(name = "note") @NotNull final String note){
        return athleteEventService.assignNoteEventToAthlete(athleteId  ,eventId ,note );
    }

    @PutMapping("/update/{athleteId}/{eventId}")
    public ResponseEntity<Object> updateNoteEventOfAthlete(@PathVariable final Long athleteId,
                                                           @PathVariable final Long eventId,
                                                           @RequestParam(name = "note") @NotNull final String note){
        return athleteEventService.updateNoteEventOfAthlete(athleteId  ,eventId ,note );
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<Object> fetchAllAthleteNoteByEventId(@PathVariable final Long eventId){
        return athleteEventService.fetchAllAthleteNoteByEventId(eventId);
    }

    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<Object> fetchAllNotesByAthleteId(@PathVariable final Long athleteId){
        return athleteEventService.fetchAllNotesByAthleteId(athleteId);
    }

    @DeleteMapping("/{athleteId}/{eventId}")
    public ResponseEntity<Object> deleteAthleteEvent(@PathVariable final Long athleteId,
                                                           @PathVariable final Long eventId){
        return athleteEventService.deleteAthleteEvent(athleteId  ,eventId);
    }


}
