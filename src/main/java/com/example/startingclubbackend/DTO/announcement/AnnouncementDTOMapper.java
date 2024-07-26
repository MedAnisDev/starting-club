package com.example.startingclubbackend.DTO.announcement;

import com.example.startingclubbackend.model.announcement.Announcement;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AnnouncementDTOMapper implements Function<Announcement , AnnouncementDTO> {
    @Override
    public AnnouncementDTO apply(Announcement announcement) {
        return new AnnouncementDTO(
                announcement.getTitle() ,
                announcement.getContent()
        );
    }
}
