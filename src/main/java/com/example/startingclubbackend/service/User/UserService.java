package com.example.startingclubbackend.service.User;

import com.example.startingclubbackend.model.user.User;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface UserService{

    User fetchUserWithEmail( final String email);

    boolean isEmailRegistered( final String email);
    boolean isPhoneNumberRegistered(final String phoneNumber) ;
}
