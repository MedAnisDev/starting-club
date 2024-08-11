package com.example.startingclubbackend.service.auth;

import com.example.startingclubbackend.DTO.auth.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<RegisterResponseDTO> register(@NotNull RegisterDTO registerDTO);

    ResponseEntity<LoginResponseDTO> login(@NotNull LoginDTO loginDTO);

    ResponseEntity<NewAccessTokenResponseDTO> refreshAccessToken(String expiredToken, String refreshToken);

    String confirmToken(String token);
}
