package com.example.startingclubbackend.service.athlete;

import com.example.startingclubbackend.DTO.athlete.AthleteDTO;
import com.example.startingclubbackend.model.user.athlete.Athlete;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AthleteService {
    Athlete saveAthlete(@NotNull Athlete athlete);

    Athlete getAthleteById(Long athleteId);

    ResponseEntity<Object> deleteAthleteById(Long athleteId) throws IOException;

    ResponseEntity<Object> getAllAthletes( long pageNumber , String sortingColumn);
    ResponseEntity<Object> getAllCustomAthletes(List<String> checkedColumns);

    ResponseEntity<Object> uploadFilesToAthlete(Long athleteId,@NotNull List<MultipartFile> files) throws IOException;

    ResponseEntity<Object> getCustomAthleteById(Long athleteId);

    ResponseEntity<Object> updateAthlete(Long athleteId,@NotNull AthleteDTO athleteDTO);
}
