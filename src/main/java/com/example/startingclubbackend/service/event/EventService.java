package com.example.startingclubbackend.service.event;

import com.example.startingclubbackend.DTO.event.EventDTO;
import com.example.startingclubbackend.model.event.Event;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;

public interface EventService {
    ResponseEntity<Object> createEvent(@NonNull EventDTO eventDTO);

    ResponseEntity<Object> fetchAllEvents(long pageNumber);

    ResponseEntity<Object> fetchEventById(Long eventId);

    ResponseEntity<Object> updateEvent(Long eventId, @NonNull EventDTO eventDTO);

    ResponseEntity<Object> deleteEventById(Long eventId);

    Event getEventById(Long eventId);
}
