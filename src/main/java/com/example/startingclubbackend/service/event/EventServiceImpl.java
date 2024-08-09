package com.example.startingclubbackend.service.event;

import com.example.startingclubbackend.DTO.event.EventDTO;
import com.example.startingclubbackend.DTO.event.EventDTOMapper;
import com.example.startingclubbackend.exceptions.custom.EventAlreadyOccurredCustomException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.event.Event;
import com.example.startingclubbackend.model.user.Admin;
import com.example.startingclubbackend.model.user.Athlete;
import com.example.startingclubbackend.repository.AthleteRepository;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository ;
    private final AthleteRepository athleteRepository ;
    private final EventDTOMapper eventDTOMapper ;

    public EventServiceImpl(EventRepository eventRepository, AthleteRepository athleteRepository, EventDTOMapper eventDTOMapper) {
        this.eventRepository = eventRepository;
        this.athleteRepository = athleteRepository;
        this.eventDTOMapper = eventDTOMapper;
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
        currentEvent.setUpdatedAt(LocalDateTime.now());
        currentEvent.setDate(eventDTO.getDate());
        currentEvent.setCreated_by(curentAdmin);

        eventRepository.save(currentEvent) ;

        final EventDTO eventDTOResponse = eventDTOMapper.apply(currentEvent) ;
        return new ResponseEntity<>(eventDTOResponse , HttpStatus.CREATED) ;
    }

    @Override
    public ResponseEntity<Object> fetchAllEvents() {
        final List<Event> events = eventRepository.fetchAllEvents();
        final List<EventDTO> eventDTOs = events.stream()
                .map(eventDTOMapper)
                .collect(Collectors.toList());
        return new ResponseEntity<>(eventDTOs , HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<Object> fetchEventById(final Long eventId) {
        final Event event = getEventById(eventId) ;
        final EventDTO eventDTOResponse = eventDTOMapper.apply(event) ;
        return new ResponseEntity<>(eventDTOResponse , HttpStatus.OK) ;

    }

    @Override
    public ResponseEntity<Object> updateEvent(final Long eventId, final @NonNull EventDTO eventDTO) {
        //check
        final Event currentEvent = getEventById(eventId) ;

        // Check if the event date is in the past
        if( currentEvent.getDate().isBefore(LocalDateTime.now())){
            throw new EventAlreadyOccurredCustomException("Cannot update an event that has already occurred") ;
        }

        currentEvent.setTitle(eventDTO.getTitle());
        currentEvent.setLocation(eventDTO.getLocation());
        currentEvent.setDescription(eventDTO.getDescription());
        currentEvent.setUpdatedAt(LocalDateTime.now());
        currentEvent.setDate(eventDTO.getDate());

        eventRepository.save(currentEvent) ;

        final EventDTO eventDTOResponse = eventDTOMapper.apply(currentEvent) ;
        return new ResponseEntity<>(eventDTOResponse , HttpStatus.OK) ;
    }

    @Transactional
    @Override
    public ResponseEntity<Object> deleteEventById(final Long eventId) {
        //check if the event exists
        Event event = getEventById(eventId);

        //remove the events from the associated athletes
        for(Athlete athlete :event.getParticipants()){
            athlete.getRegisteredEvents().remove(event) ;
            log.info("remove event Id :{} from the athlete ID :{} ",eventId,athlete.getId());
            athleteRepository.save(athlete) ;
        }

        eventRepository.deleteEventById(eventId);
        String successMessage = String.format("event with ID : %d is deleted successfully",eventId) ;
        return new ResponseEntity<>(successMessage ,HttpStatus.OK);
    }

    public Event getEventById(final Long eventId){
        return eventRepository.fetchEventById(eventId)
                .orElseThrow(()-> new ResourceNotFoundCustomException("event not found")) ;
    }
}
