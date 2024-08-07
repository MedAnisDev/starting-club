package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.ExceptionHandler;

public class DatabaseException extends ExceptionHandler {
    public DatabaseException(String message) {
        super(message);
    }
}
