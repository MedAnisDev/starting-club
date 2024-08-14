package com.example.startingclubbackend.service.athlete;

import com.example.startingclubbackend.model.user.Athlete;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AthleteService {
    Athlete saveAthlete(@NotNull Athlete athlete);

    Athlete getAthleteById(Long athleteId);

    ResponseEntity<Object> deleteAthleteById(Long athleteId);

    ResponseEntity<Object> getAllAthletes( long pageNumber , String columnName);
    ResponseEntity<Object> getAllCustomAthletes(List<String> checkedColumns);
}
