package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.CustomException;

public class InvalidTokenCustomException extends CustomException {
    public InvalidTokenCustomException(String message) {
        super(message);
    }
}
