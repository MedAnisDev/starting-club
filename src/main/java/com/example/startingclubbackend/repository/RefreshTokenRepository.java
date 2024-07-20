package com.example.startingclubbackend.repository;


import com.example.startingclubbackend.model.token.RefreshToken;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    @Query(value = "Select rt from RefreshToken rt where (rt.user.id = :userId) AND (rt.expired=false OR rt.revoked=false)")
    List<RefreshToken> fetchAllValidRefreshTokenByUserId(@NotNull @Param("userId") Long userId);

    @Query(value = "select rt from RefreshToken rt where rt.token= :refreshToken")
    Optional<RefreshToken> fetchTokenByToken(@NotNull @Param("refreshToken") final String refreshToken);
}
