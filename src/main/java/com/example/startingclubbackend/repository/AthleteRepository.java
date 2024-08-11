package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.event.Event;
import com.example.startingclubbackend.model.user.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AthleteRepository extends JpaRepository<Athlete , Long> {

    @Query("SELECT Count(a)>0 FROM Athlete a JOIN a.registeredEvents e WHERE a.id = :athleteID AND e.id = :eventId")
    boolean isAthleteRegistered(@Param("athleteID") Long athleteID ,@Param("eventId") Long eventId) ;
}
