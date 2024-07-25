package com.example.startingclubbackend.service.User;

import com.example.startingclubbackend.model.user.User;
import com.example.startingclubbackend.repository.UserRepository;
import jakarta.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository ;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User fetchUserWithEmail( final String email) {
        log.info("fetchUserWithEmail called");
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
