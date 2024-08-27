package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.CustomException;

public class EventDateCustomException extends CustomException {
    public EventDateCustomException(String message) {
        super(message);
    }
}
