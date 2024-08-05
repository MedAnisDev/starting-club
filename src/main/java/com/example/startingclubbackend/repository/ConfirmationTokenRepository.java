package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken , Long> {

    @Query("Select cf from ConfirmationToken cf where cf.token = :token")
    Optional<ConfirmationToken> fetchTokenByToken(@Param("token") String token);

}
