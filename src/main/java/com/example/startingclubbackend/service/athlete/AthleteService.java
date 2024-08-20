package com.example.startingclubbackend.service.athlete;

import com.example.startingclubbackend.model.user.Athlete;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AthleteService {
    Athlete saveAthlete(@NotNull Athlete athlete);

    Athlete getAthleteById(Long athleteId);

    ResponseEntity<Object> deleteAthleteById(Long athleteId);

    ResponseEntity<Object> getAllAthletes( long pageNumber , String sortingColumn);
    ResponseEntity<Object> getAllCustomAthletes(List<String> checkedColumns);

    ResponseEntity<Object> uploadFilesToAthlete(Long athleteId,@NotNull List<MultipartFile> files) throws IOException;

    ResponseEntity<Object> getCustomAthleteById(Long athleteId);
}
