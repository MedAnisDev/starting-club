package com.example.startingclubbackend.controller.announcements;
import com.example.startingclubbackend.model.announcement.Announcement;
import com.example.startingclubbackend.service.announcement.AnnouncementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("api/v1/announcements")
public class AnnouncementController {
    private final AnnouncementService announcementService ;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @PostMapping
    public ResponseEntity<Object> createAnnouncement(@Valid @RequestBody final Announcement announcement){
        return announcementService.createAnnouncement(announcement) ;
    }
    @GetMapping
    public ResponseEntity<Object> fetchAllAnnouncements(){
        return announcementService.fetchAllAnnouncements() ;
    }

    @GetMapping("/{announcementId}")
    public ResponseEntity<Object> fetchAnnouncement(@PathVariable final Long announcementId){
        return announcementService.fetchAnnouncementById(announcementId) ;
    }
    @PutMapping("/{announcementId}")
    public ResponseEntity<Object> updateAnnouncement(@PathVariable final Long announcementId ,@Valid @NotNull @RequestBody final Announcement announcement){
        return announcementService.updateAnnouncement(announcementId , announcement) ;
    }
}
