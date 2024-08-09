package com.example.startingclubbackend.service.Token;

import com.example.startingclubbackend.exceptions.custom.DatabaseCustomException;
import com.example.startingclubbackend.exceptions.custom.ExpiredTokenCustomException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.exceptions.custom.RevokedTokenCustomException;
import com.example.startingclubbackend.model.token.RefreshToken;
import com.example.startingclubbackend.model.user.User;
import com.example.startingclubbackend.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransactionException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
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
    public void saveAll(List<RefreshToken> refreshTokens) {
        try{
            refreshTokenRepository.saveAll(refreshTokens);
        }catch(DataAccessException da ) {
            throw new DatabaseCustomException("saving refresh token : An error occurred while accessing the database");
        }
        catch(TransactionException te){
            throw new DatabaseCustomException("saving refresh token : An error occurred while processing the transaction");
        }
    }

    @Override
    public void save(@NotNull final RefreshToken refreshToken) {
         try{
             refreshTokenRepository.save(refreshToken);
         }catch(DataAccessException da ) {
             throw new DatabaseCustomException("saving refresh token : An error occurred while accessing the database");
         }
         catch(TransactionException te){
             throw new DatabaseCustomException("saving refresh token : An error occurred while processing the transaction");
         }catch (ConstraintViolationException cve) {
             throw new DatabaseCustomException("A database constraint was violated when saving refresh token");
         }

    }

    @Override
    public RefreshToken fetchTokenByToken(final String refreshToken) {
        return refreshTokenRepository.fetchTokenByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundCustomException("refresh token not found"));
    }

    @Override
    public boolean validateRefreshToken(final String refreshToken) {
        RefreshToken refToken = fetchTokenByToken(refreshToken) ;
        if(refToken.isExpired()){
            throw new ExpiredTokenCustomException("sorry , your refresh token is expired");
        }
        if(refToken.isRevoked()){
            throw new RevokedTokenCustomException("sorry , your refresh token is revoked") ;
        }
        return true ;
    }

    @Override
    public void deleteTokenByUserId(Long userId) {
        boolean isTokenExists = refreshTokenRepository.fetchByUserId(userId);
        if(isTokenExists){
            refreshTokenRepository.deleteRefreshTokenByUserId(userId);
        }
    }

    @Value("${jwt.refresh_secret_key}")
    private String refreshSecretKey ;
    private Key getSignInKey() {
    byte [] keyBytes = Decoders.BASE64.decode(refreshSecretKey) ;
    return Keys.hmacShaKeyFor(keyBytes) ;
    }
}
