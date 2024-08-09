package com.example.startingclubbackend.DTO.athlete;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AthleteDTO (Long id ,
                          String firstname ,
                          String lastname ,
                          String email ,
                          String licenceID,
                          LocalDate dateOFBirth,
                          LocalDateTime createdAt
) {
}
