package com.example.startingclubbackend.service.announcement;

import com.example.startingclubbackend.model.announcement.Announcement;
import org.springframework.http.ResponseEntity;

public interface AnnouncementService {
    ResponseEntity<Object> createAnnouncement(final Announcement announcement) ;
}
