package com.example.startingclubbackend.DTO.athleteEvent;

import com.example.startingclubbackend.DTO.athlete.AthleteDTOMapper;
import com.example.startingclubbackend.model.event.AthleteEvent;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AthleteNoteDTOMapper implements Function<AthleteEvent , AthleteNoteDTO> {
    private final AthleteDTOMapper athleteDTOMapper ;

    public AthleteNoteDTOMapper(AthleteDTOMapper athleteDTOMapper) {
        this.athleteDTOMapper = athleteDTOMapper;
    }

    @Override
    public AthleteNoteDTO apply(AthleteEvent athleteEvent) {
        return new AthleteNoteDTO(
                athleteDTOMapper.apply(athleteEvent.getAthlete()),
                athleteEvent.getNoteEvent()
        );
    }
}
