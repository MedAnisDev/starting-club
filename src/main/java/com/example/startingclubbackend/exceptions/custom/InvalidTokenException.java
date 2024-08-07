package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidTokenException extends ExceptionHandler {
    public InvalidTokenException(String message) {
        super(message);
    }
}
