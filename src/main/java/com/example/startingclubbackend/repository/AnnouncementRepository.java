package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.announcement.Announcement;
import org.springframework.data.domain.Pageable;
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
public interface AnnouncementRepository extends JpaRepository<Announcement , Long> {
    @Query("Select a from Announcement a order by a.id")
    List<Announcement> fetchAllAnnouncementsAll(Pageable pageable);

    @Query("Select a from Announcement a where a.id = :announcementId ")
    Optional<Announcement> fetchAnnouncementById(@Param("announcementId") Long announcementId);

    @Modifying
    @Transactional
    @Query("delete from Announcement a where a.id = :announcementId")
    void deleteAnnouncementById(@Param("announcementId") Long announcementId) ;
}
