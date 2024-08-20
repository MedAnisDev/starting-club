package com.example.startingclubbackend.DTO.athleteEvent;

import com.example.startingclubbackend.DTO.athlete.AthleteDTO;

public record AthleteNoteDTO(
        AthleteDTO athleteDTO ,
        String noteEvent
) {}