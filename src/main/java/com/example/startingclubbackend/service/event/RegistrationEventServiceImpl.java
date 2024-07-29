package com.example.startingclubbackend.service.event;

import com.example.startingclubbackend.model.event.Event;
import com.example.startingclubbackend.model.user.Athlete;
import com.example.startingclubbackend.repository.AthleteRepository;
import com.example.startingclubbackend.repository.EventRepository;
import com.example.startingclubbackend.service.athlete.AthleteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RegistrationEventServiceImpl implements RegistrationEventService{
    private final AthleteService athleteService ;
    private final EventService eventService ;
    private final EventRepository eventRepository ;
    private final AthleteRepository athleteRepository ;

    public RegistrationEventServiceImpl(AthleteService athleteService, EventService eventService, EventRepository eventRepository, AthleteRepository athleteRepository) {
        this.athleteService = athleteService;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.athleteRepository = athleteRepository;
    }

    @Override
    public ResponseEntity<Object> registerAthleteToEvent(Long eventId, Long athleteId) {
        Event currentEvent = eventService.getEventById(eventId) ;
        Athlete currentAthlete = athleteService.getAthleteById(athleteId) ;

        currentEvent.getParticipants().add(currentAthlete) ;
        currentAthlete.getRegisteredEvents().add(currentEvent);

         eventRepository.save(currentEvent) ;
         athleteRepository.save(currentAthlete) ;
        String successRegistration = String.format("registration with athlete id :%d and event ID : %d is successfull ",athleteId,eventId );
        return new ResponseEntity<>(successRegistration , HttpStatus.OK) ;
    }
}
