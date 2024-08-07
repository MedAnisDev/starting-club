package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class EventAlreadyOccurredException extends ExceptionHandler {
    public EventAlreadyOccurredException(String message) {
        super(message);
    }
}
