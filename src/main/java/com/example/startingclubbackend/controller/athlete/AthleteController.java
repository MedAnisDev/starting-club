package com.example.startingclubbackend.controller.athlete;

import com.example.startingclubbackend.model.user.Athlete;
import com.example.startingclubbackend.service.athlete.AthleteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/athlete")
public class AthleteController {
    private final AthleteService athleteService ;

    public AthleteController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    @GetMapping("/{athleteId}")
    public ResponseEntity<Object> getAthleteById(@PathVariable("athleteId") final Long athleteId){
        Athlete athlete =athleteService.getAthleteById(athleteId) ;
        return new ResponseEntity<>( athlete , HttpStatus.OK) ;
    }

    @GetMapping
    public ResponseEntity<Object> getAllAthletes(){
        return athleteService.getAllAthletes() ;
    }

    @DeleteMapping("/{athleteId}")
    public ResponseEntity<Object> deleteAthleteById(@PathVariable("athleteId") final Long athleteId){
        return athleteService.deleteAthleteById(athleteId) ;
    }
}
