package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.CustomException;

public class EventAlreadyOccurredCustomException extends CustomException {
    public EventAlreadyOccurredCustomException(String message) {
        super(message);
    }
}
