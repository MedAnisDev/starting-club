package com.example.startingclubbackend.controller.event;
import com.example.startingclubbackend.DTO.event.EventDTO;
import com.example.startingclubbackend.service.event.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/events")
public class EventController {
    private final EventService eventService ;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping()
    public ResponseEntity<Object> createEvent(@Valid @RequestBody final EventDTO eventDTO){
        return eventService.createEvent(eventDTO);
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Object> uploadFilesToEvent(@PathVariable final Long eventId , @RequestParam("files") @NotNull List<MultipartFile> files) throws IOException {
        return eventService.uploadFilesToEvent(eventId ,files);
    }

    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<Object> fetchAllEvents(@PathVariable final long pageNumber ,@RequestParam(value = "columnName" ,defaultValue = "id") final String columnName){
        return eventService.fetchAllEvents(pageNumber , columnName);
    }
    @GetMapping("/{eventId}")
    public ResponseEntity<Object> fetchEventById(@PathVariable final Long eventId){
        return eventService.fetchEventById(eventId) ;
    }
    @PutMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable final Long eventId , @Valid @NotNull @RequestBody final EventDTO eventDTO){
        return eventService.updateEvent(eventId , eventDTO) ;
    }
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Object> deleteEventById(@PathVariable final Long eventId){
        return eventService.deleteEventById(eventId ) ;
    }

}
