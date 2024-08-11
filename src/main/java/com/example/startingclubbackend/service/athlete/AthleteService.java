package com.example.startingclubbackend.service.athlete;

import com.example.startingclubbackend.model.user.Athlete;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface AthleteService {
    Athlete saveAthlete(@NotNull Athlete athlete);

    Athlete getAthleteById(Long athleteId);

    ResponseEntity<Object> deleteAthleteById(Long athleteId);

    ResponseEntity<Object> getAllAthletes();
}
