package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.announcement.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement , Long> {
}
