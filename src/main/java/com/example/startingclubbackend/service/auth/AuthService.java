package com.example.startingclubbackend.service.auth;

import com.example.startingclubbackend.DTO.auth.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<RegisterResponseDTO> register(@NotNull final RegisterDTO registerDTO );

    ResponseEntity<LoginResponseDTO> login(@NotNull final LoginDTO loginDTO);

    ResponseEntity<NewAccessTokenResponseDTO> refreshAccessToken(final String expiredToken , final String refreshToken);

    String confirmToken(final String token);
}
