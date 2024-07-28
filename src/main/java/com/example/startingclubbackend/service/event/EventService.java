package com.example.startingclubbackend.service.event;

import com.example.startingclubbackend.model.event.Event;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;

public interface EventService {
    ResponseEntity<Object> createEvent(final Event event);

    ResponseEntity<Object> fetchAllEvents();

    ResponseEntity<Object> fetchEventById(final Long eventId);

    ResponseEntity<Object> updateEvent(final Long eventId,@NonNull final Event event);

    ResponseEntity<Object> deleteEventById(final Long eventId);
}
