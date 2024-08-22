package com.example.startingclubbackend.controller.event;

import com.example.startingclubbackend.service.event.EventPerformanceService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/athlete_event")
public class EventPerformanceController {
    private final EventPerformanceService eventPerformanceService;

    public EventPerformanceController(EventPerformanceService eventPerformanceService) {
        this.eventPerformanceService = eventPerformanceService;
    }

    @PostMapping("/add/{athleteId}/{eventId}")
    public ResponseEntity<Object> assignNoteEventToAthlete(@PathVariable final Long athleteId,
                                                           @PathVariable final Long eventId,
                                                           @RequestParam(name = "note") @NotNull final double note){
        return eventPerformanceService.assignNoteEventToAthlete(athleteId  ,eventId ,note );
    }

    @PutMapping("/update/{athleteId}/{eventId}")
    public ResponseEntity<Object> updateNoteEventOfAthlete(@PathVariable final Long athleteId,
                                                           @PathVariable final Long eventId,
                                                           @RequestParam(name = "note") @NotNull final double note){
        return eventPerformanceService.updateNoteEventOfAthlete(athleteId  ,eventId ,note );
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<Object> fetchAllAthleteNoteByEventId(@PathVariable final Long eventId){
        return eventPerformanceService.fetchAllAthleteNoteByEventId(eventId);
    }

    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<Object> fetchAllNotesByAthleteId(@PathVariable final Long athleteId){
        return eventPerformanceService.fetchAllNotesByAthleteId(athleteId);
    }

    @DeleteMapping("/{athleteId}/{eventId}")
    public ResponseEntity<Object> deleteAthleteEvent(@PathVariable final Long athleteId,
                                                           @PathVariable final Long eventId){
        return eventPerformanceService.deleteAthleteEvent(athleteId  ,eventId);
    }


}