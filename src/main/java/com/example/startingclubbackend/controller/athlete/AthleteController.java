package com.example.startingclubbackend.controller.athlete;


import com.example.startingclubbackend.DTO.athlete.AthleteDTO;
import com.example.startingclubbackend.service.athlete.AthleteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/athlete")
public class AthleteController {
    private final AthleteService athleteService;

    public AthleteController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    @PostMapping("/{athleteId}")
    public ResponseEntity<Object> uploadFilesToAthlete(@PathVariable final Long athleteId,
                                                       @RequestParam(name ="files") @NotNull List<MultipartFile> files) throws IOException {
        return athleteService.uploadFilesToAthlete(athleteId, files);
    }

    @GetMapping("/{athleteId}")
    public ResponseEntity<Object> getAthleteById(@PathVariable("athleteId") final Long athleteId) {
        return athleteService.getCustomAthleteById(athleteId);
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllAthletes(@RequestParam(value = "pageNumber", defaultValue = "1") final long pageNumber,
                                                 @RequestParam(value = "sortedBy", defaultValue = "id") final String sortingColumn) {
        return athleteService.getAllAthletes(pageNumber, sortingColumn);
    }

    @GetMapping("/admin/custom")
    public ResponseEntity<Object> getAllCustomAthletes(@RequestParam("checkedColumns") List<String> checkedColumns) {
        return athleteService.getAllCustomAthletes(checkedColumns);
    }

    @PutMapping("/admin/{athleteId}")
    public ResponseEntity<Object> updateAthlete(@PathVariable final Long athleteId,
                                                @NotNull @Valid @RequestBody final AthleteDTO athleteDTO) {
        return athleteService.updateAthlete(athleteId, athleteDTO);
    }

    @DeleteMapping("/admin/{athleteId}")
    public ResponseEntity<Object> deleteAthleteById(@PathVariable("athleteId") final Long athleteId) throws IOException {
        return athleteService.deleteAthleteById(athleteId);
    }
}
