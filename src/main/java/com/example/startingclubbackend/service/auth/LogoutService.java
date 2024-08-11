package com.example.startingclubbackend.service.auth;

import com.example.startingclubbackend.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository ;

    public LogoutService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        final String token = authHeader.substring(7) ;
        var currentToken = tokenRepository.fetchByToken(token)
                        .orElse(null);
        if(currentToken != null){
            currentToken.setExpired(true);
            currentToken.setRevoked(true);
            tokenRepository.save(currentToken) ;
            log.info("token is set to expired and revoked"+currentToken);
            log.info("user is logged out");
        }
    }
}
