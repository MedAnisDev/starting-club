package com.example.startingclubbackend.controller.announcements;
import com.example.startingclubbackend.DTO.announcement.AnnouncementDTO;
import com.example.startingclubbackend.service.announcement.AnnouncementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("api/v1/announcements")
public class AnnouncementController {
    private final AnnouncementService announcementService ;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @PostMapping()
    public ResponseEntity<Object> createAnnouncement(@Valid @RequestBody final AnnouncementDTO announcementDTO){
        return announcementService.createAnnouncement(announcementDTO) ;
    }
    @GetMapping()
    public ResponseEntity<Object> fetchAllAnnouncements(final long pageNumber){
        return announcementService.fetchAllAnnouncements(pageNumber) ;
    }

    @GetMapping("/{announcementId}")
    public ResponseEntity<Object> fetchAnnouncement(@PathVariable final Long announcementId){
        return announcementService.fetchAnnouncementById(announcementId) ;
    }
    @PutMapping("/{announcementId}")
    public ResponseEntity<Object> updateAnnouncement(@PathVariable final Long announcementId ,@Valid @NotNull @RequestBody final AnnouncementDTO announcementDTO){
        return announcementService.updateAnnouncement(announcementId , announcementDTO) ;
    }
    @DeleteMapping("/{announcementId}")
    public ResponseEntity<Object> deleteAnnouncement(@PathVariable final Long announcementId){
        return announcementService.deleteAnnouncementById(announcementId ) ;
    }
}
