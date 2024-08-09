package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.CustomException;

public class CustomDataIntegrityViolationCustomException extends CustomException {
    public CustomDataIntegrityViolationCustomException(String message) {
        super(message);
    }
}
