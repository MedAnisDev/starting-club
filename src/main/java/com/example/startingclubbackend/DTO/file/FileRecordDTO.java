package com.example.startingclubbackend.DTO.file;

import com.example.startingclubbackend.DTO.event.EventDTO;
import com.example.startingclubbackend.DTO.user.UserPublicDTO;
public record FileRecordDTO(
        Long id ,
        String name ,
        String type ,
        String path,
        UserPublicDTO uploadedBy ,
        EventDTO eventDTO
) {
}
