package com.example.startingclubbackend.DTO.file;

import com.example.startingclubbackend.DTO.announcement.AnnouncementDTO;
import com.example.startingclubbackend.DTO.announcement.AnnouncementDTOMapper;
import com.example.startingclubbackend.DTO.athlete.AthleteDTOMapper;
import com.example.startingclubbackend.DTO.event.EventDTOMapper;
import com.example.startingclubbackend.DTO.user.UserPublicDTOMapper;
import com.example.startingclubbackend.model.file.FileRecord;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FileRecordDTOMapper implements Function<FileRecord, FileRecordDTO> {
    private final UserPublicDTOMapper userPublicDTOMapper ;
    private final AthleteDTOMapper athleteDTOMapper ;
    private final AnnouncementDTOMapper announcementDTOMapper;
    private final EventDTOMapper eventDTOMapper;

    public FileRecordDTOMapper(UserPublicDTOMapper userPublicDTOMapper, AthleteDTOMapper athleteDTOMapper, AnnouncementDTOMapper announcementDTOMapper, EventDTOMapper eventDTOMapper) {
        this.userPublicDTOMapper = userPublicDTOMapper;
        this.athleteDTOMapper = athleteDTOMapper;
        this.announcementDTOMapper = announcementDTOMapper;
        this.eventDTOMapper = eventDTOMapper;
    }

    @Override
    public FileRecordDTO apply(FileRecord fileRecord) {
        return new FileRecordDTO(
                fileRecord.getId() ,
                fileRecord.getName(),
                fileRecord.getType(),
                fileRecord.getPath(),
                fileRecord.getUploadedBy() != null ? userPublicDTOMapper.apply(fileRecord.getUploadedBy()) : null ,

                fileRecord.getEvent() != null ? eventDTOMapper.apply(fileRecord.getEvent()) : null ,
                fileRecord.getAthlete() != null ? athleteDTOMapper.apply(fileRecord.getAthlete()) : null ,
                fileRecord.getAnnouncement() != null ? announcementDTOMapper.apply(fileRecord.getAnnouncement()) : null
        );
    }
}
