package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository {

    @Query(value="Select t from Token t where t.token= :token ")
    Optional<Token> findByToken(@Param("token") String token) ;
}
