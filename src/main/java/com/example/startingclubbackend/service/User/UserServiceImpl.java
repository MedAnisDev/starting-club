package com.example.startingclubbackend.service.User;

import com.example.startingclubbackend.model.user.User;
import com.example.startingclubbackend.repository.UserRepository;
import jakarta.validation.constraints.NotNull;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Override
    public User fetchUserWithEmail( final String email) {
        return userRepository.fetchUserWithEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found")) ;
    }

    @Override
    public boolean isEmailRegistered( final String email) {
        return userRepository.isEmailRegistered(email);
    }

    @Override
    public boolean isPhoneNumberRegistered(String phoneNumber) {
        return userRepository.isPhoneNumberRegistered(phoneNumber);
    }
}
