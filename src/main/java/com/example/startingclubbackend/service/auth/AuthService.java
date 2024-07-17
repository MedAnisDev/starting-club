package com.example.startingclubbackend.service.auth;

import com.example.startingclubbackend.DTO.auth.RegisterDTO;
import com.example.startingclubbackend.DTO.auth.RegisterResponseDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

import javax.management.relation.RoleInfoNotFoundException;

public interface AuthService {
    ResponseEntity<RegisterResponseDTO> register(@NotNull final RegisterDTO registerDTO );
}
