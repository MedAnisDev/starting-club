package com.example.startingclubbackend.service.athlete;

import com.example.startingclubbackend.model.user.Athlete;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface AthleteService {
    Athlete saveAthlete(@NotNull final Athlete athlete) ;
    Athlete getAthleteById(final Long athleteId) ;

    ResponseEntity<Object> deleteAthleteById(final Long athleteId);

    ResponseEntity<Object> getAllAthletes();
}
