package com.example.startingclubbackend.service.Token;

import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundException;
import com.example.startingclubbackend.model.token.Token;
import com.example.startingclubbackend.repository.TokenRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenServiceImpl implements TokenService{
    private final TokenRepository tokenRepository ;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Token save(@NotNull final Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public List<Token> fetchAllValidTokenByUserId(final Long userId) {
        return tokenRepository.fetchAllValidTokenByUserId(userId);
    }

    @Override
    public List<Token> saveAll(final List<Token> tokens) {
        return tokenRepository.saveAll(tokens);
    }

    @Override
    public Token fetchByToken(String expiredToken) {
        return tokenRepository.fetchByToken(expiredToken)
                .orElseThrow(() -> new ResourceNotFoundException("The token u provided could not be found in our system"));
    }
}
