package com.example.startingclubbackend.service.auth;

import com.example.startingclubbackend.DTO.auth.RegisterDTO;
import com.example.startingclubbackend.DTO.auth.RegisterResponseDTO;
import com.example.startingclubbackend.DTO.user.UserDTOMapper;
import com.example.startingclubbackend.model.Role;
import com.example.startingclubbackend.model.User;
import com.example.startingclubbackend.security.JWT.JWTService;
import com.example.startingclubbackend.service.User.UserService;
import com.example.startingclubbackend.service.role.RoleService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService{
    private final RoleService roleService;
    private final JWTService jwtService ;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService ;
    private final UserDTOMapper userDTOMapper ;

    public AuthServiceImpl(RoleService roleService, JWTService jwtService, PasswordEncoder passwordEncoder, UserService userService, UserDTOMapper userDTOMapper) {
        this.roleService = roleService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userDTOMapper = userDTOMapper;
    }


    @Override
    public ResponseEntity<RegisterResponseDTO>  register(@NotNull final RegisterDTO registerDTO)  {

        Role role = roleService.fetchRoleByName("CLIENT") ;
        var user = User.builder()
                .firstname(registerDTO.getFirstname())
                .lastName(registerDTO.getLastName())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(role)
                .build();

        User savedUser = userService.saveUser(user);
        var token = jwtService.generateToken(savedUser) ;

        RegisterResponseDTO registerResponse = RegisterResponseDTO
                .builder()
                .userDTO(userDTOMapper.apply(savedUser))
                .token(token)
                .build();

        return new ResponseEntity<>(registerResponse , HttpStatus.CREATED) ;
    }

}
