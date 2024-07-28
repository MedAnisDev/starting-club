package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("Select e from Event e")
    List<Event> fetchAllEvents();

    @Query("Select e from Event e where e.id = :eventId")
    Optional<Event> fetchEventById(@Param("eventId") final Long eventId);

    @Transactional
    @Modifying
    @Query("Delete from Event e where e.id = :eventId")
    void deleteEventById( @Param("eventId") final Long eventId);
}
