package com.example.startingclubbackend.service.event;

import org.springframework.http.ResponseEntity;

public interface RegistrationEventService {
    ResponseEntity<Object> registerAthleteToEvent(final Long eventId);
    ResponseEntity<Object> deleteAthleteFromEvent(final Long eventId , final Long athleteId) ;

}