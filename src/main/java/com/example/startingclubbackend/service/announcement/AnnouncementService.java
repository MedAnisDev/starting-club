package com.example.startingclubbackend.service.announcement;

import com.example.startingclubbackend.DTO.announcement.AnnouncementDTO;
import com.example.startingclubbackend.model.announcement.Announcement;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;

public interface AnnouncementService {
    ResponseEntity<Object> createAnnouncement(final AnnouncementDTO announcementDTO) ;

    ResponseEntity<Object> fetchAllAnnouncements();

    ResponseEntity<Object> fetchAnnouncementById(final Long announcementId);

    Announcement getAnnouncementById(final Long announcementId);

    ResponseEntity<Object> updateAnnouncement(final Long announcementId , @NonNull final AnnouncementDTO announcementDTO);

    ResponseEntity<Object> deleteAnnouncementById(final Long announcementId);
}
