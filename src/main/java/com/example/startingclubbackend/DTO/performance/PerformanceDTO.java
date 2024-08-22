package com.example.startingclubbackend.DTO.performance;

import com.example.startingclubbackend.DTO.performance.trainingSession.TrainingSessionDTO;
import com.example.startingclubbackend.DTO.user.UserPublicDTO;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PerformanceDTO {

    private Long id ;
    @DecimalMin(value = "0.0"  , message = "federation note must be at least 0")
    @DecimalMax(value = "20.0", message = "federation note must be at most 20")
    private double federationNote ;

    private LocalDateTime createdAT ;

    private LocalDateTime updatedAT;

    private UserPublicDTO createdBy ;

    private UserPublicDTO updatedBy ;

    List<TrainingSessionDTO> trainingSessionList ;

}
