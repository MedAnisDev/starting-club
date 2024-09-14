package com.example.startingclubbackend.DTO.performance.trainingSession;

import com.example.startingclubbackend.DTO.user.UserPublicDTO;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor
public class TrainingSessionDTO {
    private Long id ;

    @NotNull(message = "please enter a session date !")
    @Past(message = "You cannot assign a session note before session date")
    private LocalDate date ;

    @DecimalMin(value = "0.0"  , message = "Session note must be at least 0")
    @DecimalMax(value = "20.0", message = "Session note must be at most 20")
    private double sessionNote ;

    private LocalDateTime createdAT ;

    private LocalDateTime updatedAT;

    private UserPublicDTO createdBy ;

    private UserPublicDTO updatedBy ;
}
