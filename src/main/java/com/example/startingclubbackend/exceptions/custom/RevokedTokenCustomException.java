package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.CustomException;

public class RevokedTokenCustomException extends CustomException {
    public RevokedTokenCustomException(String message) {
        super(message);
    }
}
