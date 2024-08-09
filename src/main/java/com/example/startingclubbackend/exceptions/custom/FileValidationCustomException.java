package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.CustomException;

public class FileValidationCustomException extends CustomException {
    public FileValidationCustomException(String message) {
        super(message);
    }
}
