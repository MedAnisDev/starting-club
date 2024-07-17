package com.example.startingclubbackend.DTO.auth;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class RegisterDTO {

    @Size(min=2 ,max=10 , message = "Invalid firstname length")
    private String firstname ;

    @Size(min=2 ,max=10 , message = "Invalid lastname length")
    private String lastName;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$" , message = "Invalid email address")
    private String email;

    @Size(min = 8 , max=20 , message = "Invalid password length")
    private String password;
}
