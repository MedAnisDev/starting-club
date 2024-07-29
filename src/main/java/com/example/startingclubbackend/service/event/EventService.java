package com.example.startingclubbackend.service.event;

import com.example.startingclubbackend.DTO.event.EventDTO;
import com.example.startingclubbackend.model.event.Event;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;

public interface EventService {
    ResponseEntity<Object> createEvent(final EventDTO eventDTO );

    ResponseEntity<Object> fetchAllEvents();

    ResponseEntity<Object> fetchEventById(final Long eventId);

    ResponseEntity<Object> updateEvent(final Long eventId,@NonNull final EventDTO eventDTO);

    ResponseEntity<Object> deleteEventById(final Long eventId);
    Event getEventById(final Long eventId) ;
}
