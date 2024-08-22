package com.example.startingclubbackend.service.User;

import com.example.startingclubbackend.model.user.athlete.Athlete;
import com.example.startingclubbackend.model.user.User;

public interface UserService{

    User fetchUserWithEmail( final String email);

    boolean isEmailRegistered( final String email);
    boolean isPhoneNumberRegistered(final String phoneNumber) ;

    void enableAthleteById(final Long athleteId) ;
    Athlete getAthleteById(final Long athleteId) ;
}
