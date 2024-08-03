package com.example.startingclubbackend.DTO.file;

import com.example.startingclubbackend.DTO.user.UserPublicDTO;
import java.time.LocalDateTime;

public record FileRecordDTO(
        Long id ,
        String name ,
        String type ,
        String path,
        LocalDateTime uploadedAt ,
        UserPublicDTO uploadedBy
) {
}
