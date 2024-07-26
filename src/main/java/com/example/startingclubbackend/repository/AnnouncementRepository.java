package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.announcement.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement , Long> {
    @Query("Select a from Announcement a")
    List<Announcement> fetchAllAnnouncementsAll();

    @Query("Select a from Announcement a where a.id = :announcementId ")
    Optional<Announcement> fetchAnnouncementById(@Param("announcementId") Long announcementId);
}
