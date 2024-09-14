package com.example.startingclubbackend.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginDTO {
    @NotBlank(message = "please enter your email")
    private String email;
    @NotBlank(message = "please enter your password")
    private String password;


}
