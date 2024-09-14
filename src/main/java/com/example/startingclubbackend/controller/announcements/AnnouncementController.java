package com.example.startingclubbackend.controller.announcements;
import com.example.startingclubbackend.DTO.announcement.AnnouncementDTO;
import com.example.startingclubbackend.service.announcement.AnnouncementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    @PostMapping("/{announcementId}")
    public ResponseEntity<Object> uploadFilesToAnnouncement(@PathVariable("announcementId") final Long announcementId , @RequestParam("files") @NotNull List<MultipartFile> files) throws IOException {
        return announcementService.uploadFilesToAnnouncement(announcementId , files) ;
    }

    @GetMapping("")
    public ResponseEntity<Object> fetchAllAnnouncements(@RequestParam(value ="pageNumber" ,defaultValue="1" ) final long pageNumber ,
                                                        @RequestParam(value = "sortedBy" ,defaultValue = "id") final String sortedBy){
        return announcementService.fetchAllAnnouncements(pageNumber ,sortedBy) ;
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
    public ResponseEntity<Object> deleteAnnouncement(@PathVariable final Long announcementId) throws IOException{
        return announcementService.deleteAnnouncementById(announcementId ) ;
    }
}
