package com.example.startingclubbackend.exceptions.responseHandling;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class ApiError {
    private String errorMessage ;
    private LocalDateTime timestamp ;
    private int statusCode ;
    private List<String> errors ;

}
