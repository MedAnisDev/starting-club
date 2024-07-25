package com.example.startingclubbackend.controller.announcements;

import com.example.startingclubbackend.DTO.announcement.AnnouncementDTO;
import com.example.startingclubbackend.model.announcement.Announcement;
import com.example.startingclubbackend.service.announcement.AnnouncementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/announcement")
public class AnnouncementController {
    private final AnnouncementService announcementService ;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createAnnouncement(@RequestBody Announcement announcement){
        return announcementService.createAnnouncement(announcement) ;
    }
}
