package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.user.athlete.Athlete;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface AthleteRepository extends JpaRepository<Athlete , Long> {

    @Query("SELECT Count(a)>0 FROM Athlete a JOIN a.registeredEvents e WHERE a.id = :athleteID AND e.id = :eventId")
    boolean isAthleteRegistered(@Param("athleteID") Long athleteID ,@Param("eventId") Long eventId) ;

    @Query("SELECT a FROM Athlete a ")
    List<Athlete> fetchAllAthletes(Pageable pageable);
}
