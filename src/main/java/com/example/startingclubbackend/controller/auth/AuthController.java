package com.example.startingclubbackend.controller.auth;

import com.example.startingclubbackend.DTO.auth.LoginDTO;
import com.example.startingclubbackend.DTO.auth.LoginResponseDTO;
import com.example.startingclubbackend.DTO.auth.RegisterDTO;
import com.example.startingclubbackend.DTO.auth.RegisterResponseDTO;
import com.example.startingclubbackend.service.auth.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    public AuthController( AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterDTO registerDTO ){
        return authServiceImpl.register(registerDTO ) ;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody LoginDTO loginDTO ){
        return authServiceImpl.login(loginDTO) ;
    }

}
