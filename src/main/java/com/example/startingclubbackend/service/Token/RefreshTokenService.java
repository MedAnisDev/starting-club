package com.example.startingclubbackend.service.Token;

import com.example.startingclubbackend.model.token.RefreshToken;
import com.example.startingclubbackend.model.user.User;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface RefreshTokenService {
    String generateRefreshToken(@NotNull final User user) ;

    List<RefreshToken> fetchAllValidRefreshTokenByUserId(final Long userId);

    void saveAll(final List<RefreshToken> validRefreshTokens);

    void save(final RefreshToken refreshToken);

    RefreshToken fetchTokenByToken(final String refreshToken);

    boolean validateRefreshToken(final String refreshToken);

    void deleteTokenByUserId(final Long userId) ;
}
