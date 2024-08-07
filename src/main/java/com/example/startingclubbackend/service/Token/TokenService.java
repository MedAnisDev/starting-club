package com.example.startingclubbackend.service.Token;

import com.example.startingclubbackend.model.token.Token;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface TokenService {
    Token save(@NotNull final Token token) ;

    List<Token> fetchAllValidTokenByUserId(final Long userId);

    List<Token> saveAll(final List<Token> tokens);

    Token fetchByToken(final String expiredToken);
}
