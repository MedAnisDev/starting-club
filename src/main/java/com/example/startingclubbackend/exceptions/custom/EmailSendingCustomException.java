package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.CustomException;

public class EmailSendingCustomException extends CustomException {
    public EmailSendingCustomException(String message) {
        super(message);
    }
}
