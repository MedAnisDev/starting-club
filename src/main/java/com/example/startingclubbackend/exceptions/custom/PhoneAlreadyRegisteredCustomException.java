package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.CustomException;

public class PhoneAlreadyRegisteredCustomException extends CustomException {
    public PhoneAlreadyRegisteredCustomException(String message){
        super(message) ;
    }

}
