package com.example.startingclubbackend.service.Token;

import com.example.startingclubbackend.model.token.RefreshToken;
import com.example.startingclubbackend.model.user.User;
import com.example.startingclubbackend.repository.RefreshTokenRepository;
import com.example.startingclubbackend.security.JWT.JWTService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenRepository refreshTokenRepository ;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public String generateRefreshToken(@NotNull User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 7*24*60*60*1000))
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

    @Value("${jwt.refresh_secret_key}")
    private String refreshSecretKey ;
    private Key getSignInKey() {
    byte [] keyBytes = Decoders.BASE64.decode(refreshSecretKey) ;
    return Keys.hmacShaKeyFor(keyBytes) ;
    }
}
