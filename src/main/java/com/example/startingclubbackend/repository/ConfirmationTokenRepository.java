package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.token.ConfirmationToken;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken , Long> {
    @Query("Select cf from ConfirmationToken cf where cf.token = :token")
    Optional<ConfirmationToken> fetchByToken(@NotNull @Param("token") String token);

    @Modifying
    @Transactional
    @Query("delete from ConfirmationToken cf where cf.athlete.id = :athleteId")
    void deleteByUserId(@NotNull @Param("athleteId") Long athleteId);


    @Query("Select COUNT (ct)>0 from ConfirmationToken ct where ct.athlete.id = :athleteId")
    boolean fetchByUserId(@NotNull @Param("athleteId") Long athleteId);
}
