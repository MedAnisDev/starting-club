package com.example.startingclubbackend.security.utility;

import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundException;
import com.example.startingclubbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    final UserRepository  userRepository ;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.fetchUserWithEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not found"));
    }
}
