package com.example.startingclubbackend.DTO.athleteEvent;

import com.example.startingclubbackend.DTO.athlete.AthleteDTOMapper;
import com.example.startingclubbackend.model.event.EventPerformance;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AthleteNoteDTOMapper implements Function<EventPerformance, AthleteNoteDTO> {
    private final AthleteDTOMapper athleteDTOMapper ;

    public AthleteNoteDTOMapper(AthleteDTOMapper athleteDTOMapper) {
        this.athleteDTOMapper = athleteDTOMapper;
    }

    @Override
    public AthleteNoteDTO apply(EventPerformance eventPerformance) {
        return new AthleteNoteDTO(
                eventPerformance.getAthlete()!= null ?athleteDTOMapper.apply(eventPerformance.getAthlete()) : null,
                eventPerformance.getNoteEvent()
        );
    }
}
