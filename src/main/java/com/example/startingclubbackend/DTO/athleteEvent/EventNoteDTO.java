package com.example.startingclubbackend.DTO.athleteEvent;

import com.example.startingclubbackend.DTO.event.EventDTO;

public record EventNoteDTO(
        EventDTO eventDTO ,
        double noteEvent
) {
}
