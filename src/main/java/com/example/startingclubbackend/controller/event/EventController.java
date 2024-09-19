package com.example.startingclubbackend.controller.event;
import com.example.startingclubbackend.DTO.event.EventDTO;
import com.example.startingclubbackend.model.event.EventType;
import com.example.startingclubbackend.service.event.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/events")
@Validated
@Slf4j
public class EventController {
    private final EventService eventService ;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping()
    public ResponseEntity<Object> createEvent(@NotNull @Valid @RequestBody final EventDTO eventDTO){
        return eventService.createEvent(eventDTO);
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Object> uploadFilesToEvent(@PathVariable final Long eventId , @RequestParam(name = "files") @NotNull List<MultipartFile> files) throws IOException {
        return eventService.uploadFilesToEvent(eventId ,files);
    }

    @GetMapping("")
    public ResponseEntity<Object> fetchAllEvents(@RequestParam(value ="pageNumber" ,defaultValue="1" ) final long pageNumber ,
                                                 @RequestParam(value = "sortedBY" ,defaultValue = "id") final String sortedBy
                                                 ){
        return eventService.fetchAllEvents(pageNumber,sortedBy);
    }

    @GetMapping("/custom")
    public ResponseEntity<Object> fetchAllEventsByType(@RequestParam(value = "type" , defaultValue = "") final String type) {
        log.info("fetchAllEventsByType controller ");
        return eventService.fetchAllEventsByType(type);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> fetchEventById(@PathVariable final Long eventId){
        return eventService.fetchEventById(eventId) ;
    }
    @PutMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable final Long eventId , @NotNull @Valid @RequestBody final EventDTO eventDTO){
        return eventService.updateEvent(eventId , eventDTO) ;
    }
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Object> deleteEventById(@PathVariable final Long eventId) throws IOException{
        return eventService.deleteEventById(eventId ) ;
    }

}
