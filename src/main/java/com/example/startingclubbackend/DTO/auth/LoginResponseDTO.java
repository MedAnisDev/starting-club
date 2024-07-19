package com.example.startingclubbackend.DTO.auth;

import com.example.startingclubbackend.DTO.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginResponseDTO {
    private UserDTO userDTO ;
    private String accessToken ;
    private String refreshToken ;
}
