package com.example.startingclubbackend.service.announcement;

import com.example.startingclubbackend.DTO.announcement.AnnouncementDTO;
import com.example.startingclubbackend.model.announcement.Announcement;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;

public interface AnnouncementService {
    ResponseEntity<Object> createAnnouncement(AnnouncementDTO announcementDTO);

    ResponseEntity<Object> fetchAllAnnouncements(long pageNumber);

    ResponseEntity<Object> fetchAnnouncementById(Long announcementId);

    Announcement getAnnouncementById(Long announcementId);

    ResponseEntity<Object> updateAnnouncement(Long announcementId, @NonNull AnnouncementDTO announcementDTO);

    ResponseEntity<Object> deleteAnnouncementById(Long announcementId);

    void saveAnnouncement(@NonNull Announcement announcement);
}
