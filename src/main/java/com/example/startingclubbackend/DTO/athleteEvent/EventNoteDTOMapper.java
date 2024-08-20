package com.example.startingclubbackend.DTO.athleteEvent;

import com.example.startingclubbackend.DTO.event.EventDTOMapper;
import com.example.startingclubbackend.model.event.AthleteEvent;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EventNoteDTOMapper implements Function<AthleteEvent , EventNoteDTO>{
    private final EventDTOMapper eventDTOMapper ;

    public EventNoteDTOMapper(EventDTOMapper eventDTOMapper) {
        this.eventDTOMapper = eventDTOMapper;
    }

    @Override
    public EventNoteDTO apply(AthleteEvent athleteEvent) {
        return new EventNoteDTO(
                eventDTOMapper.apply(athleteEvent.getEvent()),
                athleteEvent.getNoteEvent()
        ) ;
    }
}
