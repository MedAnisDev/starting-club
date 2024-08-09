package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.CustomException;

public class ExpiredTokenCustomException extends CustomException {
    public ExpiredTokenCustomException(String message) {
        super(message);
    }
}
