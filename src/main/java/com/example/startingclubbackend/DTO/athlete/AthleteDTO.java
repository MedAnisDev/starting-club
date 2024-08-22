package com.example.startingclubbackend.DTO.athlete;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AthleteDTO {
    private Long id ;

    @NotBlank(message = "Please fill first name field")
    private String firstname ;

    @NotBlank(message = "Please fill last name field")
    private String lastname ;

    private String email ;

    private String password ;

    @NotBlank(message = "Please fill phone number field")
    private String phoneNumber ;

    @NotBlank(message = "Please fill licence ID field")
    private String licenceID;

    @NotNull
    @Past
    private LocalDate dateOfBirth;

    @NotBlank
    private String branch ;

    private LocalDateTime createdAt ;

    private boolean hasMedal;

}
