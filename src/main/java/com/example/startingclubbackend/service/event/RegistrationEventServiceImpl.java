package com.example.startingclubbackend.service.event;

import com.example.startingclubbackend.model.event.Event;
import com.example.startingclubbackend.model.user.Athlete;
import com.example.startingclubbackend.repository.AthleteRepository;
import com.example.startingclubbackend.repository.EventRepository;
import com.example.startingclubbackend.service.athlete.AthleteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationEventServiceImpl implements RegistrationEventService{
    private final EventService eventService ;
    private final EventRepository eventRepository ;
    private final AthleteService athleteService ;
    private final AthleteRepository athleteRepository ;

    public RegistrationEventServiceImpl(EventService eventService, EventRepository eventRepository, AthleteService athleteService, AthleteRepository athleteRepository) {
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.athleteService = athleteService;
        this.athleteRepository = athleteRepository;
    }

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

    public boolean isAthleteRegistered(final Long eventId ,final Long athleteID){
        return athleteRepository.isAthleteRegistered(athleteID, eventId) ;
    }
}
