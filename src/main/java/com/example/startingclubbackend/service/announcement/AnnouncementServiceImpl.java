package com.example.startingclubbackend.service.announcement;

import com.example.startingclubbackend.DTO.announcement.AnnouncementDTO;
import com.example.startingclubbackend.DTO.announcement.AnnouncementDTOMapper;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundException;
import com.example.startingclubbackend.model.announcement.Announcement;
import com.example.startingclubbackend.model.user.Admin;
import com.example.startingclubbackend.repository.AnnouncementRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
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
    private final AnnouncementDTOMapper announcementDTOMapper ;

    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository, AnnouncementDTOMapper announcementDTOMapper) {
        this.announcementRepository = announcementRepository;
        this.announcementDTOMapper = announcementDTOMapper;
    }


    @Override
    public ResponseEntity<Object> createAnnouncement(final AnnouncementDTO announcementDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = (Admin) authentication.getPrincipal() ;

        log.info("Authentication Principal: " + authentication.getPrincipal());
        log.info("Authentication Authorities: " + authentication.getAuthorities());

        final Announcement currentAnnouncement = new Announcement() ;
        currentAnnouncement.setTitle(announcementDTO.getTitle()) ;
        currentAnnouncement.setContent(announcementDTO.getContent()) ;
        currentAnnouncement.setCreatedBy(admin) ;

        announcementRepository.save(currentAnnouncement) ;

        final AnnouncementDTO announcementDTOResponse = announcementDTOMapper.apply(currentAnnouncement) ;
        return new ResponseEntity<>(announcementDTOResponse , HttpStatus.OK);
    }
    @Override
    public ResponseEntity<Object> fetchAllAnnouncements() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication Principal: " + authentication.getPrincipal());
        log.info("Authentication Authorities: " + authentication.getAuthorities());

        final List<Announcement> announcements = announcementRepository.fetchAllAnnouncementsAll();
        final List<AnnouncementDTO> announcementDTOList = announcements.stream()
                                                                .map(announcementDTOMapper)
                                                                .toList();
        return new ResponseEntity<>(announcementDTOList , HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<Object> fetchAnnouncementById(final Long announcementId) {
        final Announcement currentAnnouncement = getAnnouncementById(announcementId) ;
        final AnnouncementDTO announcementDTOResponse = announcementDTOMapper.apply(currentAnnouncement) ;
        return  new ResponseEntity<>(announcementDTOResponse  ,HttpStatus.OK) ;
    }

    @Override
    public Announcement getAnnouncementById(final Long announcementId) {
        return announcementRepository.fetchAnnouncementById(announcementId)
                .orElseThrow(()-> new ResourceNotFoundException("Announcement not found")) ;
    }

    @Override
    public ResponseEntity<Object> updateAnnouncement(final Long announcementId ,@NonNull final AnnouncementDTO announcementDTO) {
        final Announcement currentAnnouncement = getAnnouncementById(announcementId) ;

        currentAnnouncement.setTitle(announcementDTO.getTitle());
        currentAnnouncement.setContent(announcementDTO.getContent());
        currentAnnouncement.setUpdatedAt(LocalDateTime.now());
        announcementRepository.save(currentAnnouncement) ;

        log.info("current Announcement : "+currentAnnouncement);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication Principal: " + authentication.getPrincipal());
        log.info("Authentication Authorities: " + authentication.getAuthorities());

        final AnnouncementDTO announcementDTOResponse = announcementDTOMapper.apply(currentAnnouncement) ;
        return new ResponseEntity<>(announcementDTOResponse , HttpStatus.OK) ;
    }

    @Transactional
    @Override
    public ResponseEntity<Object> deleteAnnouncementById(final Long announcementId) {
        announcementRepository.deleteAnnouncementById(announcementId);
        final String successResponse = String.format("Announcement with ID :  %d deleted successfully", announcementId);
        return new ResponseEntity<>(successResponse , HttpStatus.OK);
    }
}
