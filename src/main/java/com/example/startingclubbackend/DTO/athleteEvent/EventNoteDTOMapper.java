package com.example.startingclubbackend.DTO.athleteEvent;

import com.example.startingclubbackend.DTO.event.EventDTOMapper;
import com.example.startingclubbackend.model.event.EventPerformance;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EventNoteDTOMapper implements Function<EventPerformance, EventNoteDTO>{
    private final EventDTOMapper eventDTOMapper ;

    public EventNoteDTOMapper(EventDTOMapper eventDTOMapper) {
        this.eventDTOMapper = eventDTOMapper;
    }

    @Override
    public EventNoteDTO apply(EventPerformance eventPerformance) {
        return new EventNoteDTO(
                eventPerformance.getEvent() != null ?eventDTOMapper.apply(eventPerformance.getEvent()) : null,
                eventPerformance.getNoteEvent()
        ) ;
    }
}
