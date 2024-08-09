package com.example.startingclubbackend.DTO.athlete;

import com.example.startingclubbackend.model.role.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AthleteDTO (Long id ,
                          String firstname ,
                          String lastname ,
                          String email ,
                          String password ,
                          boolean isEnabled ,
                          LocalDateTime createdAt,
                          Role role ,
                          String licenceID,
                          LocalDate dateOFBirth
) {
}
