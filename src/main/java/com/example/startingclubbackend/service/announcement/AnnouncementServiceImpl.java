package com.example.startingclubbackend.service.announcement;

import com.example.startingclubbackend.model.announcement.Announcement;
import com.example.startingclubbackend.model.user.Admin;
import com.example.startingclubbackend.repository.AnnouncementRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        final Announcement currentAnnouncement = Announcement
                .builder()
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .createdAt(LocalDateTime.now())
                .createdBy(admin)
                .build();
        announcementRepository.save(currentAnnouncement) ;
        return new ResponseEntity<>(currentAnnouncement , HttpStatus.CREATED);
    }
    @Override
    public ResponseEntity<Object> fetchAllAnnouncements() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication Principal: " + authentication.getPrincipal());
        log.info("Authentication Authorities: " + authentication.getAuthorities());

        final List<Announcement> announcements = announcementRepository.fetchAllAnnouncementsAll();
        return new ResponseEntity<>(announcements , HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<Object> fetchAnnouncementById(final Long announcementId) {
        final Announcement currentAnnouncement = getAnnouncementById(announcementId) ;
        return  new ResponseEntity<>(currentAnnouncement  ,HttpStatus.OK) ;
    }

    @Override
    public Announcement getAnnouncementById(final Long announcementId) {
        return announcementRepository.fetchAnnouncementById(announcementId)
                .orElseThrow(()-> new IllegalArgumentException("Announcement not found")) ;
    }

    @Override
    public ResponseEntity<Object> updateAnnouncement(final Long announcementId ,@NonNull final Announcement announcement) {
        final Announcement currentAnnouncement = getAnnouncementById(announcementId) ;
        currentAnnouncement.setTitle(announcement.getTitle());
        currentAnnouncement.setContent(announcement.getContent());
        announcementRepository.save(currentAnnouncement) ;

        log.info("current Announcement : "+currentAnnouncement);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication Principal: " + authentication.getPrincipal());
        log.info("Authentication Authorities: " + authentication.getAuthorities());

        return new ResponseEntity<>(currentAnnouncement , HttpStatus.OK) ;
    }

}
