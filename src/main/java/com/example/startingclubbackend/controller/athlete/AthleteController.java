package com.example.startingclubbackend.controller.athlete;


import com.example.startingclubbackend.service.athlete.AthleteService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/athlete")
public class AthleteController {
    private final AthleteService athleteService ;

    public AthleteController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    @PostMapping("/{athleteId}")
    public ResponseEntity<Object> uploadFilesToAthlete(@PathVariable final Long athleteId ,@RequestParam("files") @NotNull List<MultipartFile> files)throws IOException {
        return athleteService.uploadFilesToAthlete(athleteId , files) ;
    }

    @GetMapping("/{athleteId}")
    public ResponseEntity<Object> getCustomAthleteById(@PathVariable("athleteId") final Long athleteId){
        return athleteService.getCustomAthleteById(athleteId);
    }

    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<Object> getAllAthletes(@PathVariable("pageNumber")final long pageNumber ,@RequestParam(value = "sortingColumn" ,defaultValue = "id") final String sortingColumn){
        return athleteService.getAllAthletes(pageNumber,sortingColumn) ;
    }

    @GetMapping("/custom")
    public ResponseEntity<Object> getAllCustomAthletes(@RequestParam List<String> checkedColumns){
        return athleteService.getAllCustomAthletes(checkedColumns) ;
    }

    @DeleteMapping("/{athleteId}")
    public ResponseEntity<Object> deleteAthleteById(@PathVariable("athleteId") final Long athleteId){
        return athleteService.deleteAthleteById(athleteId) ;
    }
}
