package com.example.startingclubbackend.service.Token;

import com.example.startingclubbackend.model.token.RefreshToken;
import com.example.startingclubbackend.model.user.User;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenService {
    String generateRefreshToken(@NotNull final User user) ;

    List<RefreshToken> fetchAllValidRefreshTokenByUserId(final Long userId);

    List<RefreshToken> saveAll(final List<RefreshToken> validRefreshTokens);

    RefreshToken save(final RefreshToken refreshToken);

    RefreshToken fetchTokenByToken(final String refreshToken);

    boolean validateRefreshToken(final String refreshToken);
}
