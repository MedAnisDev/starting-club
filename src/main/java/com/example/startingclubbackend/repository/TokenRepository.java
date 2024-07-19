package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.token.Token;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {

    @Query(value="Select t from Token t where t.token= :token ")
    Optional<Token> findByToken(@Param("token") String token) ;

    @Query(value = "Select t from Token t where (t.user.id = :userId) AND (t.revoked = false OR t.expired = false)")
    List<Token> fetchAllValidTokenByUserId(@NotNull @Param("userId") Long userId);
}