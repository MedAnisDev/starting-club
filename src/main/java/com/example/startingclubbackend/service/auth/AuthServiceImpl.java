package com.example.startingclubbackend.service.auth;

import com.example.startingclubbackend.DTO.auth.LoginDTO;
import com.example.startingclubbackend.DTO.auth.LoginResponseDTO;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService{
    private final RoleService roleService;
    private final JWTService jwtService ;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService ;
    private final UserDTOMapper userDTOMapper ;
    private final AuthenticationManager authenticationManager ;

    public AuthServiceImpl(RoleService roleService, JWTService jwtService, PasswordEncoder passwordEncoder, UserService userService, UserDTOMapper userDTOMapper, AuthenticationManager authenticationManager) {
        this.roleService = roleService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userDTOMapper = userDTOMapper;
        this.authenticationManager = authenticationManager;
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

        final RegisterResponseDTO registerResponse = RegisterResponseDTO
                .builder()
                .userDTO(userDTOMapper.apply(savedUser))
                .token(token)
                .build();

        return new ResponseEntity<>(registerResponse , HttpStatus.CREATED) ;
    }

    @Override
    public ResponseEntity<LoginResponseDTO> login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(),
                loginDTO.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.fetchUserWithEmail(loginDTO.getEmail());
        var token = jwtService.generateToken(user);

        final LoginResponseDTO loginRepsonse = LoginResponseDTO.builder()
                .token(token)
                .userDTO(userDTOMapper.apply(user))
                .build() ;

        return new ResponseEntity<>(loginRepsonse , HttpStatus.CREATED);
    }

}
