package com.example.startingclubbackend.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class RegisterDTO {

    @NotBlank(message = "first name is required")
    @Size(min=2 ,max=10 , message = "Invalid firstname length")
    private String firstname ;

    @NotBlank(message = "last name is required")
    @Size(min=2 ,max=10 , message = "Invalid lastname length")
    private String lastname;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$" , message = "Invalid email address")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 8 , max=20 , message = "Invalid password length")
    private String password;

    @NotBlank(message = "phone number is required")
    @Size(min =8 , max=8 , message = "Invalid phone number length , please enter an 8 characters number")
    private String phoneNumber ;

    private String licenceID ;

    private LocalDate dateOfBirth ;




}
