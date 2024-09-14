package com.example.startingclubbackend.service.event;

import com.example.startingclubbackend.DTO.event.EventDTO;
import com.example.startingclubbackend.model.event.Event;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EventService {
    ResponseEntity<Object> createEvent(@NonNull EventDTO eventDTO);

    ResponseEntity<Object> fetchAllEvents(long pageNumber , String columnName);
    ResponseEntity<Object> fetchAllEventsByType(String type);

    ResponseEntity<Object> fetchEventById(Long eventId);

    ResponseEntity<Object> updateEvent(Long eventId, @NonNull EventDTO eventDTO);

    ResponseEntity<Object> deleteEventById(Long eventId) throws IOException;

    Event getEventById(Long eventId);

    ResponseEntity<Object> uploadFilesToEvent(Long eventId, @NotNull List<MultipartFile> files) throws IOException;
    void saveEvent(@NonNull Event event) ;
}
