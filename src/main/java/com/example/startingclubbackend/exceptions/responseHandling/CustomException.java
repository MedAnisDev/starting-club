package com.example.startingclubbackend.exceptions.responseHandling;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message) ;
    }

    public ResponseEntity<Object> handleResponse(String errors , String message ,@NotNull HttpStatus status){
        List<String> details = new ArrayList<>();
        details.add(errors);

        ApiError apiError = ApiError.builder()
                .errorMessage(message)
                .errors(details)
                .timestamp(LocalDateTime.now())
                .statusCode(status.value())
                .build();
        return ResponseEntityBuilder.buildResponse(apiError) ;
    }
}
