package com.example.startingclubbackend.service.User;

import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.user.athlete.Athlete;
import com.example.startingclubbackend.model.user.User;
import com.example.startingclubbackend.repository.AthleteRepository;
import com.example.startingclubbackend.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository ;

    private final AthleteRepository athleteRepository;

    public UserServiceImpl(UserRepository userRepository, AthleteRepository athleteRepository) {
        this.userRepository = userRepository;
        this.athleteRepository = athleteRepository;
    }

    @Override
    public User fetchUserWithEmail( final String email) {
        return userRepository.fetchUserWithEmail(email)
                .orElseThrow(() -> new ResourceNotFoundCustomException("user not found")) ;
    }

    @Override
    public boolean isEmailRegistered( final String email) {
        return userRepository.isEmailRegistered(email);
    }

    @Override
    public boolean isPhoneNumberRegistered(String phoneNumber) {
        return userRepository.isPhoneNumberRegistered(phoneNumber);
    }

    @Override
    public void enableAthleteById(Long athleteId) {
        Athlete athlete = getAthleteById(athleteId) ;
        athlete.setEnable(true);
        athleteRepository.save(athlete);

    }

    @Override
    public Athlete getAthleteById(Long athleteId) {
        return athleteRepository.findById(athleteId)
                .orElseThrow(() -> new ResourceNotFoundCustomException("Athlete not found"));
    }
}
