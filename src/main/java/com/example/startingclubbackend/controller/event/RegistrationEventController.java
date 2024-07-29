package com.example.startingclubbackend.controller.event;

import com.example.startingclubbackend.service.event.RegistrationEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/register_event")
public class RegistrationEventController {
    private final RegistrationEventService registrationEventService ;

    public RegistrationEventController(RegistrationEventService registrationEventService) {
        this.registrationEventService = registrationEventService;
    }

    @PostMapping("/{eventId}/{athleteId}")
    public ResponseEntity<Object> registerAthleteToEvent(@PathVariable final Long eventId , @PathVariable final Long athleteId){
        return registrationEventService.registerAthleteToEvent(eventId , athleteId) ;
    }
}
