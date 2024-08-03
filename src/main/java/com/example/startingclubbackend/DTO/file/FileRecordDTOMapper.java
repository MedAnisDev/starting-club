package com.example.startingclubbackend.DTO.file;

import com.example.startingclubbackend.DTO.user.UserPublicDTO;
import com.example.startingclubbackend.DTO.user.UserPublicDTOMapper;
import com.example.startingclubbackend.model.file.FileRecord;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FileRecordDTOMapper implements Function<FileRecord, FileRecordDTO> {
    private final UserPublicDTOMapper userPublicDTOMapper ;

    public FileRecordDTOMapper(UserPublicDTOMapper userPublicDTOMapper) {
        this.userPublicDTOMapper = userPublicDTOMapper;
    }

    @Override
    public FileRecordDTO apply(FileRecord fileRecord) {
        return new FileRecordDTO(
                fileRecord.getId() ,
                fileRecord.getName(),
                fileRecord.getType(),
                fileRecord.getPath(),
                fileRecord.getUploadedAt() ,
                fileRecord.getUploadedBy() != null ? userPublicDTOMapper.apply(fileRecord.getUploadedBy()) : null
        );
    }
}
