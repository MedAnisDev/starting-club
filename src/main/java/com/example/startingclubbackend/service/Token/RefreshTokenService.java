package com.example.startingclubbackend.service.Token;

import com.example.startingclubbackend.model.token.RefreshToken;
import com.example.startingclubbackend.model.user.User;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface RefreshTokenService {
    String generateRefreshToken(@NotNull User user);

    List<RefreshToken> fetchAllValidRefreshTokenByUserId(Long userId);

    void saveAll(List<RefreshToken> validRefreshTokens);

    void save(RefreshToken refreshToken);

    RefreshToken fetchTokenByToken(String refreshToken);

    boolean validateRefreshToken(String refreshToken);

    void deleteTokenByUserId(Long userId);
}
