package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.CustomException;

public class EmailAlreadyRegisteredCustomException extends CustomException {
    public EmailAlreadyRegisteredCustomException(String message){
        super(message);
    }
}
