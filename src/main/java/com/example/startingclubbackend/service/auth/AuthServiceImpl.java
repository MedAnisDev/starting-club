package com.example.startingclubbackend.service.auth;

import com.example.startingclubbackend.DTO.auth.*;
import com.example.startingclubbackend.DTO.user.UserDTOMapper;
import com.example.startingclubbackend.model.role.Role;
import com.example.startingclubbackend.model.token.RefreshToken;
import com.example.startingclubbackend.model.token.Token;
import com.example.startingclubbackend.model.user.Athlete;
import com.example.startingclubbackend.model.user.User;
import com.example.startingclubbackend.model.token.TokenType;
import com.example.startingclubbackend.repository.AthleteRepository;
import com.example.startingclubbackend.repository.UserRepository;
import com.example.startingclubbackend.security.JWT.JWTService;
import com.example.startingclubbackend.service.Token.RefreshTokenService;
import com.example.startingclubbackend.service.Token.TokenService;
import com.example.startingclubbackend.service.User.UserService;
import com.example.startingclubbackend.service.athlete.AthleteService;
import com.example.startingclubbackend.service.role.RoleService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;


@Service
@Slf4j
public class AuthServiceImpl implements AuthService{
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final RoleService roleService;
    private final JWTService jwtService ;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService ;
    private final UserDTOMapper userDTOMapper ;
    private final AuthenticationManager authenticationManager ;
   private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService ;
    private final AthleteService athleteService ;

    @Value("${jwt.refresh_expiration}")
    private long expirationRefreshTokenDuration ;
    public AuthServiceImpl(RoleService roleService, JWTService jwtService, PasswordEncoder passwordEncoder, UserService userService, UserDTOMapper userDTOMapper, AuthenticationManager authenticationManager, TokenService tokenService, RefreshTokenService refreshTokenService,AthleteService athleteService) {
        this.roleService = roleService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userDTOMapper = userDTOMapper;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.athleteService = athleteService;
    }


    @Override
    public ResponseEntity<RegisterResponseDTO> register(@NotNull final RegisterDTO registerDTO)  {

        if(userService.isEmailRegistered(registerDTO.getEmail())){
            throw new IllegalArgumentException("Sorry, that email is already registered");
        }

        if (userService.isPhoneNumberRegistered(registerDTO.getPhoneNumber())) {
            throw new IllegalArgumentException("Sorry, that phone number is already registered.");
        }

        Role role = roleService.fetchRoleByName("ATHLETE") ;

        Athlete athlete = Athlete.builder()
            .firstname(registerDTO.getFirstname())
            .lastname(registerDTO.getLastname())
            .email(registerDTO.getEmail())
            .password(passwordEncoder.encode(registerDTO.getPassword()))
            .phoneNumber(registerDTO.getPhoneNumber())
            .licenceID(registerDTO.getLicenceID())
            .dateOFBirth(registerDTO.getDateOfBirth())
            .createAt(LocalDateTime.now())
            .role(role)
            .isEnabled(true)
            .build();

        User savedUser = athleteService.saveAthlete(athlete);
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
        revokeAllUsersAccessToken(user);
        revokeAllUsersRefreshToken(user);

        var accessToken = generateAndSaveAccessToken(user);
        String refreshToken = generateAndSaveUserRefreshToken(user);

       final LoginResponseDTO loginResponse = LoginResponseDTO.builder()
               .accessToken(accessToken)
               .refreshToken(refreshToken)
               .userDTO(userDTOMapper.apply(user))
               .build();
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NewAccessTokenResponseDTO> refreshAccessToken(String expiredToken ,String refreshToken) {
        final Token currentAccessToken = tokenService.fetchByToken(expiredToken) ;
        final User currentUser = currentAccessToken.getUser() ;

        final RefreshToken currentRefreshToken = refreshTokenService.fetchTokenByToken(refreshToken) ;
        final boolean isRefreshTokenValid = refreshTokenService.validateRefreshToken(currentRefreshToken.getToken()) ;

        if( !isRefreshTokenValid ){ // if the token has been expired or revoked
            throw new IllegalStateException("refresh token has expired or revoked");
        }
        if(!currentRefreshToken.getUser().getId().equals(currentUser.getId())){ // and refresh token belongs to the user that has expired token
            throw new IllegalStateException("the refresh and access token provided does not belong to the same user");
        }

        revokeAllUsersAccessToken(currentUser);
        String newAccessToken = generateAndSaveAccessToken(currentUser);
        final NewAccessTokenResponseDTO newAccessTokenResponse = NewAccessTokenResponseDTO
                        .builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken)
                        .build();
        return new  ResponseEntity<>(newAccessTokenResponse , HttpStatus.OK );
    }

    private String  generateAndSaveAccessToken(User user){
        var accessToken = jwtService.generateToken(user);
        saveUserAccessToken(user ,accessToken);
        return accessToken ;
    }

    private void saveUserAccessToken(@NotNull User user ,@NotNull String accessToken){
        Token jwt = Token.builder()
                .user(user)
                .token(accessToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.save(jwt) ;
        logger.info("access Token saved successfully");

    }
    public String generateAndSaveUserRefreshToken(User user){
        var refreshToken = refreshTokenService.generateRefreshToken(user);
        saveUserRefreshToken(user , refreshToken);
        return refreshToken ;

    }

    private void saveUserRefreshToken(@NotNull User user ,@NotNull String refreshToken){


        Date expirationDate = new Date(System.currentTimeMillis() + expirationRefreshTokenDuration);
        Date issuedDate = new Date(System.currentTimeMillis()) ;

        RefreshToken jwt = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expired(false)
                .revoked(false)
                .issuedAt(issuedDate)
                .expiresAt(expirationDate) //30 minutes
                .build();
        refreshTokenService.save(jwt) ;
        logger.info("refresh Token saved successfully");
    }


    private void revokeAllUsersAccessToken(@NotNull User user){
        var validTokens = tokenService.fetchAllValidTokenByUserId(user.getId()) ;
        if(validTokens.isEmpty()){
            logger.info("there is no previous access token to revoke");
            return ;
        }
        validTokens.forEach(t ->{
            t.setRevoked(true);
            t.setExpired(true);
        });
        logger.info("previous access tokens revoked successfully");
        tokenService.saveAll(validTokens) ;
    }

    private void revokeAllUsersRefreshToken(@NotNull User user){
        var validRefreshTokens = refreshTokenService.fetchAllValidRefreshTokenByUserId(user.getId()) ;
        if(validRefreshTokens.isEmpty()){
            logger.info("there is no previous refresh token to revoke");
            return ;
        }
        validRefreshTokens.forEach(rt -> {
            rt.setRevoked(true);
            rt.setExpired(true);
        });
        logger.info("previous refresh tokens revoked successfully");
        refreshTokenService.saveAll(validRefreshTokens) ;
    }
}
