package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.ExceptionHandler;

public class FileValidationException extends ExceptionHandler {
    public FileValidationException(String message) {
        super(message);
    }
}
