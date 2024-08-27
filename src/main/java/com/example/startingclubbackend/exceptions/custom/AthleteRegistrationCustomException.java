package com.example.startingclubbackend.exceptions.custom;

import com.example.startingclubbackend.exceptions.responseHandling.CustomException;

public class AthleteRegistrationCustomException extends CustomException {
    public AthleteRegistrationCustomException(String message) {
        super(message);
    }
}
