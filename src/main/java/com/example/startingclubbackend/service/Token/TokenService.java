package com.example.startingclubbackend.service.Token;

import com.example.startingclubbackend.model.token.Token;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface TokenService {
    Token save(@NotNull Token token) ;
    List<Token> fetchAllValidTokenByUserId(Long userId);

    void saveAll(List<Token> tokens);

    Token fetchByToken(String expiredToken);

    void deleteByUserId(Long userId) ;
}
