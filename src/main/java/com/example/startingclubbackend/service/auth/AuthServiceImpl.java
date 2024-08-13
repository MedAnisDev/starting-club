package com.example.startingclubbackend.service.auth;

import com.example.startingclubbackend.DTO.auth.*;
import com.example.startingclubbackend.DTO.user.UserDTOMapper;
import com.example.startingclubbackend.exceptions.custom.EmailAlreadyRegisteredCustomException;
import com.example.startingclubbackend.exceptions.custom.ExpiredTokenCustomException;
import com.example.startingclubbackend.exceptions.custom.InvalidTokenCustomException;
import com.example.startingclubbackend.exceptions.custom.PhoneAlreadyRegisteredCustomException;
import com.example.startingclubbackend.model.role.Role;
import com.example.startingclubbackend.model.token.ConfirmationToken;
import com.example.startingclubbackend.model.token.RefreshToken;
import com.example.startingclubbackend.model.token.Token;
import com.example.startingclubbackend.model.user.Athlete;
import com.example.startingclubbackend.model.user.User;
import com.example.startingclubbackend.model.token.TokenType;
import com.example.startingclubbackend.security.JWT.JWTService;
import com.example.startingclubbackend.service.Token.ConfirmationTokenService;
import com.example.startingclubbackend.service.Token.RefreshTokenService;
import com.example.startingclubbackend.service.Token.TokenService;
import com.example.startingclubbackend.service.User.UserService;
import com.example.startingclubbackend.service.athlete.AthleteService;
import com.example.startingclubbackend.service.email.EmailSenderService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService ;

    @Value("${jwt.refresh_expiration}")
    private long expirationRefreshTokenDuration ;
    public AuthServiceImpl(RoleService roleService, JWTService jwtService, PasswordEncoder passwordEncoder, UserService userService, UserDTOMapper userDTOMapper, AuthenticationManager authenticationManager, TokenService tokenService, RefreshTokenService refreshTokenService, AthleteService athleteService, ConfirmationTokenService confirmationTokenService, EmailSenderService emailSenderService) {
        this.roleService = roleService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userDTOMapper = userDTOMapper;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.athleteService = athleteService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
    }


    @Override
    @Transactional
    public ResponseEntity<RegisterResponseDTO> register(@NotNull final RegisterDTO registerDTO)  {
        //validate User info
        if(userService.isEmailRegistered(registerDTO.getEmail())){
            throw new EmailAlreadyRegisteredCustomException("Email " + registerDTO.getEmail() + " is already registered");
        }
        if (userService.isPhoneNumberRegistered(registerDTO.getPhoneNumber())) {
            throw new PhoneAlreadyRegisteredCustomException("phone number " + registerDTO.getPhoneNumber() + " is already registered.");
        }
        //build and save athlete
        Role role = roleService.fetchRoleByName("ROLE_ATHLETE") ;
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
            .enable(false)
            .build();
        Athlete savedAthlete = athleteService.saveAthlete(athlete);

        //get tokens
        String refreshToken = refreshTokenService.generateRefreshToken(savedAthlete);
        String confirmationToken = confirmationTokenService.generateConfirmationToken(savedAthlete);

        //send email confirmation
        final String link = "http://localhost:8082/api/v1/auth/confirm?token=" + confirmationToken;
        emailSenderService.sendEmail(
                savedAthlete.getEmail() ,
                emailSenderService.emailTemplateConfirmation(savedAthlete.getFirstname() ,link )
        );

        //response
        final RegisterResponseDTO registerResponse = RegisterResponseDTO
                .builder()
                .userDTO(userDTOMapper.apply(savedAthlete))
                .refreshToken(refreshToken)
                .confirmationToken(confirmationToken)
                .build();
        return new ResponseEntity<>(registerResponse , HttpStatus.CREATED) ;
    }

    @Override
    @Transactional
    public ResponseEntity<LoginResponseDTO> login(LoginDTO loginDTO) {
        User user = userService.fetchUserWithEmail(loginDTO.getEmail());
        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified. Please verify your account.");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(),
                loginDTO.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        revokeAllUsersAccessToken(user);
        revokeAllUsersRefreshToken(user);

        var accessToken = generateAndSaveAccessToken(user);
        String refreshToken = generateAndSaveUserRefreshToken(user);

        //response
       final LoginResponseDTO loginResponse = LoginResponseDTO.builder()
               .accessToken(accessToken)
               .refreshToken(refreshToken)
               .userDTO(userDTOMapper.apply(user))
               .build();
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<NewAccessTokenResponseDTO> refreshAccessToken(String expiredToken ,String refreshToken) {
        final Token currentAccessToken = tokenService.fetchByToken(expiredToken) ;
        final User currentUser = currentAccessToken.getUser() ;

        final RefreshToken currentRefreshToken = refreshTokenService.fetchTokenByToken(refreshToken) ;
        final boolean isRefreshTokenValid = refreshTokenService.validateRefreshToken(currentRefreshToken.getToken()) ;

        if( !isRefreshTokenValid ){ // if the token has been expired or revoked
            throw new InvalidTokenCustomException("refresh token has expired or revoked");
        }
        if(!currentRefreshToken.getUser().getId().equals(currentUser.getId())){ // and refresh token belongs to the user that has expired token
            throw new InvalidTokenCustomException("the refresh and access token provided does not belong to the same user");
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

    @Override
    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken =confirmationTokenService.fetchTokenByToken(token) ;
        if(confirmationToken.getConfirmedAt() != null){
           return confirmationTokenService.getAlreadyConfirmedPage();
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt() ;
        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new ExpiredTokenCustomException("confirmation Token already expired") ;
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableAthleteById(confirmationToken.getAthlete().getId());

        log.info("User with enable : "+ confirmationToken.getAthlete().isEnable());

        return confirmationTokenService.getConfirmationPage();
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
