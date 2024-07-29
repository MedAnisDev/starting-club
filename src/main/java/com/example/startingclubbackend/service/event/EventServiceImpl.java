package com.example.startingclubbackend.service.event;

import com.example.startingclubbackend.DTO.event.EventDTO;
import com.example.startingclubbackend.model.event.Event;
import com.example.startingclubbackend.model.user.Admin;
import com.example.startingclubbackend.repository.EventRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository ;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public ResponseEntity<Object> createEvent(final EventDTO eventDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Admin curentAdmin = (Admin) auth.getPrincipal();

        log.info("Authentication Principal: " + auth.getPrincipal());
        log.info("Authentication Authorities: " + auth.getAuthorities());

        final Event currentEvent = new Event() ;
        currentEvent.setTitle(eventDTO.getTitle());
        currentEvent.setLocation(eventDTO.getLocation());
        currentEvent.setDescription(eventDTO.getDescription());
        currentEvent.setCreatedAt(LocalDateTime.now());
        currentEvent.setUpdatedAt(LocalDateTime.now());
        currentEvent.setDate(eventDTO.getDate());
        currentEvent.setCreated_by(curentAdmin);

        eventRepository.save(currentEvent) ;
        return new ResponseEntity<>(currentEvent , HttpStatus.CREATED) ;
    }

    @Override
    public ResponseEntity<Object> fetchAllEvents() {
        final List<Event> events = eventRepository.fetchAllEvents();
        return new ResponseEntity<>(events , HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<Object> fetchEventById(final Long eventId) {
        final Event event = getEventById(eventId) ;
        return new ResponseEntity<>(event , HttpStatus.OK) ;

    }

    @Override
    public ResponseEntity<Object> updateEvent(final Long eventId, final @NonNull EventDTO eventDTO) {
        final Event currentEvent = getEventById(eventId) ;

        if( currentEvent.getDate().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Cannot update an event that has already occurred") ;
        }

        currentEvent.setTitle(eventDTO.getTitle());
        currentEvent.setLocation(eventDTO.getLocation());
        currentEvent.setDescription(eventDTO.getDescription());
        currentEvent.setUpdatedAt(LocalDateTime.now());
        currentEvent.setDate(eventDTO.getDate());

        eventRepository.save(currentEvent) ;
        return new ResponseEntity<>(currentEvent , HttpStatus.CREATED) ;
    }

    @Transactional
    @Override
    public ResponseEntity<Object> deleteEventById(final Long eventId) {
        if( eventRepository.fetchEventById(eventId).isEmpty() ){
            throw new IllegalArgumentException("the eventId does not exist") ;
        }
        eventRepository.deleteEventById(eventId);
        String successMessage = String.format("event with ID : %d is deleted successfully",eventId) ;
        return new ResponseEntity<>(successMessage ,HttpStatus.OK);
    }

    public Event getEventById(final Long eventId){
        return eventRepository.fetchEventById(eventId)
                .orElseThrow(()-> new IllegalArgumentException("event not found")) ;
    }
}
