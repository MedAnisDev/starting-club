package com.example.startingclubbackend.service.User;

import com.example.startingclubbackend.model.User;
import com.example.startingclubbackend.repository.UserRepository;
import jakarta.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository ;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(@NotNull final User user) {
        return userRepository.save(user) ;
    }
}
