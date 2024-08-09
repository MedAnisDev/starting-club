package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.CustomException;

public class ResourceNotFoundCustomException extends CustomException {
    public ResourceNotFoundCustomException(String message) {
        super(message);
    }
}
