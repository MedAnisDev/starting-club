package com.example.startingclubbackend.service.Token;

import com.example.startingclubbackend.exceptions.custom.DatabaseCustomException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.token.Token;
import com.example.startingclubbackend.repository.TokenRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransactionException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService{
    private final TokenRepository tokenRepository ;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Token save(@NotNull final Token token) {
        try{
            return tokenRepository.save(token);
        }catch (ConstraintViolationException cve) {
            throw new DatabaseCustomException("A database constraint was violated when saving access token");
        }
    }

    @Override
    public List<Token> fetchAllValidTokenByUserId(final Long userId) {
        return tokenRepository.fetchAllValidTokenByUserId(userId);
    }

    @Override
    public void saveAll(final List<Token> tokens) {
        try{
            tokenRepository.saveAll(tokens);
        }catch(DataAccessException da ) {
            throw new DatabaseCustomException("saving access token : An error occurred while accessing the database");
        }
        catch(TransactionException te){
        throw new DatabaseCustomException("saving access token : An error occurred while processing the transaction");
        }
    }

    @Override
    public Token fetchByToken(String expiredToken) {
        return tokenRepository.fetchByToken(expiredToken)
                .orElseThrow(() -> new ResourceNotFoundCustomException("The token u provided could not be found in our system"));
    }

    @Override
    public void deleteByUserId(Long userId) {
        boolean isTokenExists = tokenRepository.fetchByUserId(userId) ;
        log.info("isTokenExists"+isTokenExists);
        if(isTokenExists){
            tokenRepository.deleteTokenByUserId(userId);
            log.info("token is deleted");
        }
    }
}
