package com.example.startingclubbackend.service.announcement;

import com.example.startingclubbackend.DTO.announcement.AnnouncementDTO;
import com.example.startingclubbackend.model.announcement.Announcement;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AnnouncementService {
    ResponseEntity<Object> createAnnouncement(AnnouncementDTO announcementDTO);

    ResponseEntity<Object> fetchAllAnnouncements(long pageNumber , String sortedBy);

    ResponseEntity<Object> fetchAnnouncementById(Long announcementId);

    Announcement getAnnouncementById(Long announcementId);

    ResponseEntity<Object> updateAnnouncement(Long announcementId, @NonNull AnnouncementDTO announcementDTO);

    ResponseEntity<Object> deleteAnnouncementById(Long announcementId) throws IOException;

    void saveAnnouncement(@NonNull Announcement announcement);
    ResponseEntity<Object> uploadFilesToAnnouncement(final Long announcementId , @NotNull List<MultipartFile> files ) throws IOException;
}
