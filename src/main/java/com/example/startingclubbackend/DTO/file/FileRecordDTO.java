package com.example.startingclubbackend.DTO.file;

import com.example.startingclubbackend.DTO.event.EventDTO;
import com.example.startingclubbackend.DTO.user.UserPublicDTO;
import com.example.startingclubbackend.model.event.Event;

import java.time.LocalDateTime;

public record FileRecordDTO(
        Long id ,
        String name ,
        String type ,
        String path,
        UserPublicDTO uploadedBy ,
        EventDTO eventDTO
) {
}
