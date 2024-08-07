package com.example.startingclubbackend.security.JWT;

import com.example.startingclubbackend.exceptions.custom.ExpiredTokenException;
import com.example.startingclubbackend.exceptions.custom.InvalidTokenException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundException;
import com.example.startingclubbackend.exceptions.custom.RevokedTokenException;
import com.example.startingclubbackend.repository.TokenRepository;
import com.example.startingclubbackend.security.utility.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService ;

    private final   CustomUserDetailsService customUserDetailsService  ;

     private final TokenRepository tokenRepository ;

    public JWTAuthenticationFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization") ;
        if ((authHeader == null) || !authHeader.startsWith("Bearer") ){
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7) ;
        String email = jwtService.extractEmailFromJwt(jwt);
        if (email == null || SecurityContextHolder.getContext().getAuthentication() != null) { // true if USER email don't exist
                                                                        // or the user is already authenticated
            log.info("USER email don't exist || user is already authenticated");
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtService.validateToken(jwt)) {
            log.info("JWT validation failed");
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails user = customUserDetailsService.loadUserByUsername(email) ;
        log.info("user loaded {} :", user);
        if (!jwtService.isTokenValid(user , jwt)){
            log.info("token expired or it's the not the same user");
            filterChain.doFilter(request, response);
            return ;
        }

        var isTokenValid = tokenRepository.findByToken(jwt).map(t -> (!t.isExpired() && !t.isRevoked())).orElse(false) ;
        var TokenSaved = tokenRepository.findByToken(jwt).orElse(null);

        if(!isTokenValid){
            throw new InvalidTokenException("token invalid");
        }
        if(TokenSaved ==null){
            throw new ResourceNotFoundException("Invalid not found");
        }

        if(jwtService.isTokenExpired(jwt)){
            throw new ExpiredTokenException("token expired") ;
        }

        if(TokenSaved.isRevoked()){
            throw new RevokedTokenException("token revoked") ;
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);



    }
}
