package com.example.startingclubbackend.controller.event;

import com.example.startingclubbackend.service.event.RegistrationEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/register_event")
public class RegistrationEventController {
    private final RegistrationEventService registrationEventService ;

    public RegistrationEventController(RegistrationEventService registrationEventService) {
        this.registrationEventService = registrationEventService;
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Object> registerAthleteToEvent(@PathVariable final Long eventId){
        return registrationEventService.registerAthleteToEvent(eventId) ;
    }
    @DeleteMapping("/admin/{eventId}/{athleteId}")
    public ResponseEntity<Object> deleteAthleteFromEvent( @PathVariable final Long eventId ,@PathVariable final Long athleteId) {
        return  registrationEventService.deleteAthleteFromEvent(eventId , athleteId) ;
    }
}
