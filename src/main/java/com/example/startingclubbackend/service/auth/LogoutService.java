package com.example.startingclubbackend.service.auth;

import com.example.startingclubbackend.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository ;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
       log.info("custom logout called");
        final String authHeader = request.getHeader("Authorization");
        log.info("authHeader : "+authHeader);

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            log.info("authHeader == null or authHeader do not start with Bearer");
            return;
        }

        final String token = authHeader.substring(7) ;
        log.info("token : "+token);
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
