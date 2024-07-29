package com.example.startingclubbackend.service.athlete;

import com.example.startingclubbackend.model.user.Athlete;
import jakarta.validation.constraints.NotNull;

public interface AthleteService {
    Athlete saveAthlete(@NotNull final Athlete athlete) ;
    Athlete getAthleteById(final Long eventId) ;
}
