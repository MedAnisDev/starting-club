package com.example.startingclubbackend.service.event;

import com.example.startingclubbackend.DTO.athlete.AthleteDTO;
import com.example.startingclubbackend.DTO.athlete.AthleteDTOMapper;
import com.example.startingclubbackend.model.event.Event;
import com.example.startingclubbackend.model.user.Athlete;
import com.example.startingclubbackend.repository.AthleteRepository;
import com.example.startingclubbackend.repository.EventRepository;
import com.example.startingclubbackend.service.athlete.AthleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegistrationEventServiceImpl implements RegistrationEventService{
    private final EventService eventService ;
    private final EventRepository eventRepository ;
    private final AthleteRepository athleteRepository ;
    private final AthleteDTOMapper athleteDTOMapper ;
    private AthleteService athleteService ;

    public RegistrationEventServiceImpl(EventService eventService, EventRepository eventRepository, AthleteRepository athleteRepository, AthleteDTOMapper athleteDTOMapper) {
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.athleteRepository = athleteRepository;
        this.athleteDTOMapper = athleteDTOMapper;
    }

    @Autowired
    @Lazy
    public void setAthleteService(AthleteService athleteService){
        this.athleteService = athleteService ;
    }

    @Override
    @Transactional
    public ResponseEntity<Object> registerAthleteToEvent(final Long eventId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Athlete currentAthlete = (Athlete) auth.getPrincipal();
        Long athleteID =currentAthlete.getId() ;


        if(isAthleteRegistered(eventId , athleteID)){
            String errorMessage = String.format("sorry , we cannot register athlete with ID : %d he is already registered ",athleteID);
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
        Event currentEvent = eventService.getEventById(eventId);

        currentEvent.getParticipants().add(currentAthlete);
        currentAthlete.getRegisteredEvents().add(currentEvent);

        eventRepository.save(currentEvent);
        athleteRepository.save(currentAthlete);
        String successRegistration = String.format("registration of athlete with ID: %d successfully ", athleteID);
        return new ResponseEntity<>(successRegistration, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> deleteAthleteFromEvent(final Long eventId ,final Long athleteId) {
        Event currentEvent = eventService.getEventById(eventId);
        Athlete currentAthlete = athleteService.getAthleteById(athleteId) ;

        currentEvent.getParticipants().remove(currentAthlete) ;
        currentAthlete.getRegisteredEvents().remove(currentEvent) ;

        eventRepository.save(currentEvent);
        athleteRepository.save(currentAthlete);

        String successRegistration = String.format("removal of athlete with ID : %d from event with ID: %d is successful  ", athleteId , eventId);
        return new ResponseEntity<>(successRegistration , HttpStatus.OK) ;
    }

    @Override
    @Transactional
    public void deleteAthleteFromAllEvents(final Long athleteId) {
        Athlete currentAthlete = athleteService.getAthleteById(athleteId) ;
        List<Event> registeredEvents = currentAthlete.getRegisteredEvents() ;

        if(!registeredEvents.isEmpty()){
            for(Event currentEvent :registeredEvents){
                currentEvent.getParticipants().remove(currentAthlete) ;
                eventRepository.save(currentEvent);
            }
            currentAthlete.getRegisteredEvents().clear();
            athleteRepository.save(currentAthlete);
        }
    }

    @Override
    public ResponseEntity<Object> getAllParticipants(Long eventId) {
        final Event currentEvent = eventService.getEventById(eventId);
        final List<Athlete> participants = currentEvent.getParticipants();

        if(participants.isEmpty()){
            String errorMessage= String.format("sorry , event with title '%s' does not contain any registered athlete yet",currentEvent.getTitle()) ;
            return new ResponseEntity<>(errorMessage ,HttpStatus.OK);
        }
        List<AthleteDTO> participantsDTOList = participants.stream().map(athleteDTOMapper).toList();

        return new ResponseEntity<>(participantsDTOList ,HttpStatus.OK);
    }


    public boolean isAthleteRegistered(final Long eventId ,final Long athleteID){
        return athleteRepository.isAthleteRegistered(athleteID, eventId) ;
    }
}
