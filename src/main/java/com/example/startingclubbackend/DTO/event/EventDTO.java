package com.example.startingclubbackend.DTO.event;

import com.example.startingclubbackend.DTO.user.UserPublicDTO;
import com.example.startingclubbackend.DTO.user.UserPublicDTOMapper;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EventDTO {

    private Long id ;

    @NotBlank(message = "title cannot be empty")
    @Size(max = 50, message = "Event title cannot be longer than 50 characters")
    private String title;

    @NotBlank(message = "location cannot be empty")
    private String location;

    @NotBlank(message = "description cannot be empty")
    private String description;

    @NotNull(message = "Event date cannot be null")
    @Future(message = "Event date must be in the future")
    private LocalDateTime date;

    private UserPublicDTO userPublicDTO ;
}
