package com.example.startingclubbackend.service.event;

import com.example.startingclubbackend.DTO.athleteEvent.AthleteNoteDTO;
import com.example.startingclubbackend.DTO.athleteEvent.AthleteNoteDTOMapper;
import com.example.startingclubbackend.DTO.athleteEvent.EventNoteDTO;
import com.example.startingclubbackend.DTO.athleteEvent.EventNoteDTOMapper;
import com.example.startingclubbackend.exceptions.custom.DatabaseCustomException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.event.AthleteEvent;
import com.example.startingclubbackend.model.event.Event;
import com.example.startingclubbackend.model.user.Athlete;
import com.example.startingclubbackend.repository.AthleteEventRepository;
import com.example.startingclubbackend.service.athlete.AthleteService;
import jakarta.validation.constraints.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AthleteEventServiceImpl implements AthleteEventService {
    private final AthleteEventRepository athleteEventRepository;
    private final AthleteService athleteService;
    private final EventService eventService;
    private final AthleteNoteDTOMapper athleteNoteDTOMapper;
    private final EventNoteDTOMapper eventNoteDTOMapper;

    public AthleteEventServiceImpl(AthleteEventRepository athleteEventRepository, AthleteService athleteService, EventService eventService, AthleteNoteDTOMapper athleteNoteDTOMapper, EventNoteDTOMapper eventNoteDTOMapper) {
        this.athleteEventRepository = athleteEventRepository;
        this.athleteService = athleteService;
        this.eventService = eventService;
        this.athleteNoteDTOMapper = athleteNoteDTOMapper;
        this.eventNoteDTOMapper = eventNoteDTOMapper;
    }

    @Override
    public ResponseEntity<Object> assignNoteEventToAthlete(final Long athleteId, final Long eventId, final String note) {
        Athlete currAthlete = athleteService.getAthleteById(athleteId);
        Event currEvent = eventService.getEventById(eventId);

        AthleteEvent athleteEvent = new AthleteEvent();
        athleteEvent.setAthlete(currAthlete);
        athleteEvent.setEvent(currEvent);
        athleteEvent.setNoteEvent(note);
        saveAthleteEvent(athleteEvent);
        return new ResponseEntity<>("note you assigned to an athlete for an event is saved successfully ", HttpStatus.CREATED);
    }

    @Override
    public void saveAthleteEvent(@NotNull final AthleteEvent athleteEvent) {
        try {
            athleteEventRepository.save(athleteEvent);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseCustomException("error with saving athlete_event");
        }
    }

    @Override
    public ResponseEntity<Object> fetchAllAthleteNoteByEventId(final Long eventId) {

        final List<AthleteNoteDTO> athleteNoteDTOList = athleteEventRepository.fetchAllAthleteNoteByEventId(eventId)
                .stream()
                .map(athleteNoteDTOMapper)
                .toList();

        return new ResponseEntity<>(athleteNoteDTOList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> fetchAllNotesByAthleteId(final Long athleteId) {

        final List<EventNoteDTO> EventNoteDTOList = athleteEventRepository.fetchAllNotesByAthleteId(athleteId)
                .stream()
                .map(eventNoteDTOMapper)
                .toList();
        return new ResponseEntity<>(EventNoteDTOList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateNoteEventOfAthlete(final Long athleteId, final Long eventId, final @NotNull String note) {
        final AthleteEvent currAthleteEvent = getAthleteEventByAthleteIdAndEventId(athleteId, eventId);
        currAthleteEvent.setNoteEvent(note);
        saveAthleteEvent(currAthleteEvent);
        return new ResponseEntity<>("new note updated successfully", HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<Object> deleteAthleteEvent(final Long athleteId, final Long eventId) {
        AthleteEvent athleteEvent = getAthleteEventByAthleteIdAndEventId(athleteId, eventId);
        athleteEventRepository.deleteAthleteEvent(athleteId, eventId);
        return new ResponseEntity<>("delete action successful", HttpStatus.OK);
    }

    public AthleteEvent getAthleteEventByAthleteIdAndEventId(final Long athleteId, final Long eventId) {
        return athleteEventRepository.fetchWithAthleteIdAndEventId(athleteId, eventId)
                .orElseThrow(() -> new ResourceNotFoundCustomException("resource not found"));
    }
}
