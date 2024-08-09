package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.CustomException;

public class DatabaseCustomException extends CustomException {
    public DatabaseCustomException(String message) {
        super(message);
    }
}
