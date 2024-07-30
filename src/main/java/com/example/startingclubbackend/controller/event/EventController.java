package com.example.startingclubbackend.controller.event;
import com.example.startingclubbackend.DTO.event.EventDTO;
import com.example.startingclubbackend.model.announcement.Announcement;
import com.example.startingclubbackend.model.event.Event;
import com.example.startingclubbackend.service.event.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping()
    public ResponseEntity<Object> fetchAllEvents(){
        return eventService.fetchAllEvents();
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
