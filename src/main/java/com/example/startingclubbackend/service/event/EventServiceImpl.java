package com.example.startingclubbackend.service.event;

import com.example.startingclubbackend.DTO.event.EventDTO;
import com.example.startingclubbackend.DTO.event.EventDTOMapper;
import com.example.startingclubbackend.exceptions.custom.DatabaseCustomException;
import com.example.startingclubbackend.exceptions.custom.EventAlreadyOccurredCustomException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.event.Event;
import com.example.startingclubbackend.model.event.EventType;
import com.example.startingclubbackend.model.file.FileRecord;
import com.example.startingclubbackend.model.user.Admin;
import com.example.startingclubbackend.model.user.Athlete;
import com.example.startingclubbackend.repository.AthleteRepository;
import com.example.startingclubbackend.repository.EventRepository;
import com.example.startingclubbackend.repository.FileRepository;
import com.example.startingclubbackend.service.file.FileService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Request;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository ;
    private final AthleteRepository athleteRepository ;
    private final EventDTOMapper eventDTOMapper ;
    private final FileService fileService ;

    public EventServiceImpl(EventRepository eventRepository, AthleteRepository athleteRepository, EventDTOMapper eventDTOMapper, FileService fileService) {
        this.eventRepository = eventRepository;
        this.athleteRepository = athleteRepository;
        this.eventDTOMapper = eventDTOMapper;
        this.fileService = fileService;
    }

    @Override
    public ResponseEntity<Object> createEvent(@NonNull final EventDTO eventDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Admin curentAdmin = (Admin) auth.getPrincipal();

        log.info("Authentication Principal: " + auth.getPrincipal());
        log.info("Authentication Authorities: " + auth.getAuthorities());

        EventType eventType =EventType.valueOf(eventDTO.getType()) ;

        final Event currentEvent = new Event() ;
        currentEvent.setTitle(eventDTO.getTitle());
        currentEvent.setLocation(eventDTO.getLocation());
        currentEvent.setDescription(eventDTO.getDescription());
        currentEvent.setUpdatedAt(LocalDateTime.now());
        currentEvent.setDate(eventDTO.getDate());
        currentEvent.setType(eventType);
        currentEvent.setCreated_by(curentAdmin);

        eventRepository.save(currentEvent) ;

        final EventDTO eventDTOResponse = eventDTOMapper.apply(currentEvent) ;
        return new ResponseEntity<>(eventDTOResponse , HttpStatus.CREATED) ;
    }

    @Override
    public ResponseEntity<Object> fetchAllEvents(final long pageNumber) {
        Pageable pageable = PageRequest.of((int)pageNumber - 1 , 5) ;
        final List<EventDTO> eventDTOs = eventRepository.fetchAllEvents(pageable).stream()
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
        EventType eventType = EventType.valueOf(eventDTO.getType()) ;

        currentEvent.setTitle(eventDTO.getTitle());
        currentEvent.setLocation(eventDTO.getLocation());
        currentEvent.setDescription(eventDTO.getDescription());
        currentEvent.setUpdatedAt(LocalDateTime.now());
        currentEvent.setDate(eventDTO.getDate());
        currentEvent.setType(eventType);

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

    @Transactional
    @Override
    public ResponseEntity<Object> uploadFilesToEvent(final Long eventId,final List<MultipartFile> files) throws IOException {
        final Event currEvent = getEventById(eventId);
        final List<FileRecord> eventFiles = new ArrayList<>();
        //saving each event into FileRecord
        for (MultipartFile file : files) {
            final FileRecord currentFile = fileService.handleFile(file);
            currentFile.setEvent(currEvent);
            fileService.saveFile(currentFile);

            eventFiles.add(currentFile);
        }
        //save all FileRecord list  into this current event
        currEvent.setFiles(eventFiles);
        saveEvent(currEvent);

        return new ResponseEntity<>("files added to this event successfully", HttpStatus.OK);
    }
    @Override
    public void saveEvent(@NonNull final Event event) {
        try {
            eventRepository.save(event);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseCustomException("error with saving this event");
        }
    }
}
