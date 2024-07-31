package com.example.startingclubbackend.DTO.announcement;

import com.example.startingclubbackend.DTO.user.UserPublicDTOMapper;
import com.example.startingclubbackend.model.announcement.Announcement;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AnnouncementDTOMapper implements Function<Announcement , AnnouncementDTO> {

    private final UserPublicDTOMapper userPublicDTOMapper ;

    public AnnouncementDTOMapper(UserPublicDTOMapper userPublicDTOMapper) {
        this.userPublicDTOMapper = userPublicDTOMapper;
    }

    @Override
    public AnnouncementDTO apply(Announcement announcement) {
        return new AnnouncementDTO(
                announcement.getId() ,
                announcement.getTitle() ,
                announcement.getContent(),
                announcement.getCreatedAt(),
                announcement.getUpdatedAt(),
                announcement .getCreatedBy() != null ? userPublicDTOMapper.apply(announcement.getCreatedBy()) : null
        );
    }
}
