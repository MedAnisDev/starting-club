package com.example.startingclubbackend.DTO.announcement;

import com.example.startingclubbackend.DTO.user.UserPublicDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AnnouncementDTO{

    private Long id ;

    @NotBlank(message = "title cannot be empty")
    @Size(max = 50, message = "Event title cannot be longer than 50 characters")
    private String title;

    private String content;

    private LocalDateTime createdAt ;

    private LocalDateTime updatedAt ;

    private UserPublicDTO createdBY ;
}
