package com.example.startingclubbackend.service.User;

import com.example.startingclubbackend.model.User;
import jakarta.validation.constraints.NotNull;

public interface UserService{
    User saveUser(@NotNull final User user) ;

    User fetchUserWithEmail(@NotNull final String email);
}
