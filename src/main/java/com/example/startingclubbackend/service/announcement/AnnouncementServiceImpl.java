package com.example.startingclubbackend.service.announcement;

import com.example.startingclubbackend.DTO.announcement.AnnouncementDTO;
import com.example.startingclubbackend.DTO.announcement.AnnouncementDTOMapper;
import com.example.startingclubbackend.exceptions.custom.DatabaseCustomException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.announcement.Announcement;
import com.example.startingclubbackend.model.event.Event;
import com.example.startingclubbackend.model.file.FileRecord;
import com.example.startingclubbackend.model.user.Admin;
import com.example.startingclubbackend.repository.AnnouncementRepository;
import com.example.startingclubbackend.service.file.FileService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class AnnouncementServiceImpl implements AnnouncementService{
    private final AnnouncementRepository announcementRepository ;
    private final AnnouncementDTOMapper announcementDTOMapper ;
    private final FileService fileService ;

    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository, AnnouncementDTOMapper announcementDTOMapper, FileService fileService) {
        this.announcementRepository = announcementRepository;
        this.announcementDTOMapper = announcementDTOMapper;
        this.fileService = fileService;
    }


    @Override
    public ResponseEntity<Object> createAnnouncement(final AnnouncementDTO announcementDTO) {
        //get creator of announcement (admin)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = (Admin) authentication.getPrincipal();

        final Announcement currentAnnouncement = new Announcement();
        currentAnnouncement.setTitle(announcementDTO.getTitle());
        currentAnnouncement.setContent(announcementDTO.getContent());
        currentAnnouncement.setCreatedBy(admin);
        saveAnnouncement(currentAnnouncement);

        final AnnouncementDTO announcementDTOResponse = announcementDTOMapper.apply(currentAnnouncement);
        return new ResponseEntity<>(announcementDTOResponse, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<Object> fetchAllAnnouncements(final long pageNumber ,final String columnName) {

        Sort sort = Sort.by(Sort.Order.desc(columnName).nullsLast()) ;
        Pageable pageable = PageRequest.of(
                (int)pageNumber -1 ,
                5 ,
                sort
        );
        final List<AnnouncementDTO> announcementDTOList = announcementRepository.fetchAllAnnouncements(pageable)
                .stream()
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
                .orElseThrow(()-> new ResourceNotFoundCustomException("Announcement not found")) ;
    }

    @Override
    public ResponseEntity<Object> updateAnnouncement(final Long announcementId ,@NonNull final AnnouncementDTO announcementDTO) {
        final Announcement currentAnnouncement = getAnnouncementById(announcementId) ;

        currentAnnouncement.setTitle(announcementDTO.getTitle());
        currentAnnouncement.setContent(announcementDTO.getContent());
        currentAnnouncement.setUpdatedAt(LocalDateTime.now());
        announcementRepository.save(currentAnnouncement) ;

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

    @Override
    public void saveAnnouncement(@NonNull final Announcement announcement) {
        try {
             announcementRepository.save(announcement);
        } catch (DataIntegrityViolationException  e) {
            throw new DatabaseCustomException("Title or Description cannot be null");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> uploadFilesToAnnouncement(final Long announcementId , @NotNull List<MultipartFile> files ) throws IOException {
        final Announcement currAnnouncement = getAnnouncementById(announcementId) ;

        final List<FileRecord> announcementFiles = new ArrayList<>();
        for (MultipartFile file : files){
            //saving each announcement into FileRecord
            final FileRecord fileRecord = fileService.handleFile(file) ;
            fileRecord.setAnnouncement(currAnnouncement);
            fileService.saveFile(fileRecord);
        }
        //save all FileRecord list  into this current announcement
        currAnnouncement.setFiles(announcementFiles);
        saveAnnouncement(currAnnouncement);

        return new ResponseEntity<>("files added to this announcement successfully",HttpStatus.OK) ;
    }
}
