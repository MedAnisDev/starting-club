package com.example.startingclubbackend.controller.auth;

import com.example.startingclubbackend.DTO.auth.*;
import com.example.startingclubbackend.model.token.RefreshToken;
import com.example.startingclubbackend.service.auth.AuthService;
import com.example.startingclubbackend.service.auth.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController( AuthServiceImpl authServiceImpl) {
        this.authService = authServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register( @RequestBody RegisterDTO registerDTO ){
        return authService.register(registerDTO ) ;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO ){
        return authService.login(loginDTO) ;
    }

    @GetMapping("/refresh/{refreshToken}")
    public ResponseEntity<NewAccessTokenResponseDTO> refreshAccessToken(@PathVariable("refreshToken") final String refreshToken , @RequestParam("expiredToken") final String expiredToken){
        return authService.refreshAccessToken( expiredToken , refreshToken) ;
    }

}
