package com.example.startingclubbackend.DTO.event;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class EventDTO {
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

    public EventDTO(String title, String location, String description, LocalDateTime date) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
