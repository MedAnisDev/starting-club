package com.example.startingclubbackend.service.Token;

import com.example.startingclubbackend.exceptions.custom.ExpiredTokenException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundException;
import com.example.startingclubbackend.exceptions.custom.RevokedTokenException;
import com.example.startingclubbackend.model.token.RefreshToken;
import com.example.startingclubbackend.model.user.User;
import com.example.startingclubbackend.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenRepository refreshTokenRepository ;

    @Value("${jwt.refresh_expiration}")
    private long expirationRefreshTokenDuration ;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public String generateRefreshToken(@NotNull User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationRefreshTokenDuration))
                .signWith(getSignInKey() , SignatureAlgorithm.HS256)
                .compact() ;
    }

    @Override
    public List<RefreshToken> fetchAllValidRefreshTokenByUserId(final Long userId) {
        return refreshTokenRepository.fetchAllValidRefreshTokenByUserId(userId);
    }

    @Override
    public List<RefreshToken> saveAll(List<RefreshToken> refreshTokens) {
        return refreshTokenRepository.saveAll(refreshTokens);
    }

    @Override
    public RefreshToken save(@NotNull final RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken fetchTokenByToken(final String refreshToken) {
        return refreshTokenRepository.fetchTokenByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("refresh token not found"));
    }

    @Override
    public boolean validateRefreshToken(final String refreshToken) {
        RefreshToken refToken = fetchTokenByToken(refreshToken) ;
        if(refToken.isExpired()){
            throw new ExpiredTokenException("sorry , your refresh token is expired");
        }
        if(refToken.isRevoked()){
            throw new RevokedTokenException("sorry , your refresh token is revoked") ;
        }
        return true ;
    }

    @Value("${jwt.refresh_secret_key}")
    private String refreshSecretKey ;
    private Key getSignInKey() {
    byte [] keyBytes = Decoders.BASE64.decode(refreshSecretKey) ;
    return Keys.hmacShaKeyFor(keyBytes) ;
    }
}
