package com.example.startingclubbackend.service.announcement;

import com.example.startingclubbackend.model.announcement.Announcement;
import com.example.startingclubbackend.model.user.Admin;
import com.example.startingclubbackend.repository.AnnouncementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AnnouncementServiceImpl implements AnnouncementService{
    private final AnnouncementRepository announcementRepository ;

    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }


    @Override
    public ResponseEntity<Object> createAnnouncement(final Announcement announcement) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication Principal: " + authentication.getPrincipal());
        log.info("Authentication Authorities: " + authentication.getAuthorities());

        Admin admin = (Admin) authentication.getPrincipal() ;
        Announcement currentAnnouncement = Announcement
                .builder()
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .createdAt(LocalDateTime.now())
                .createdBy(admin)
                .build();
        announcementRepository.save(currentAnnouncement) ;
        return new ResponseEntity<>(currentAnnouncement , HttpStatus.CREATED);
    }
}
