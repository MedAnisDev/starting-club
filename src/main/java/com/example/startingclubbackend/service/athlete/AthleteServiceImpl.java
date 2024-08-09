package com.example.startingclubbackend.service.athlete;

import com.example.startingclubbackend.exceptions.custom.DatabaseCustomException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.user.Athlete;
import com.example.startingclubbackend.repository.AthleteRepository;
import com.example.startingclubbackend.service.Token.ConfirmationTokenService;
import com.example.startingclubbackend.service.Token.RefreshTokenService;
import com.example.startingclubbackend.service.Token.TokenService;
import com.example.startingclubbackend.service.event.RegistrationEventService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AthleteServiceImpl implements AthleteService{
    private final TokenService tokenService ;
    private final RefreshTokenService refreshTokenService ;

    private final AthleteRepository athleteRepository ;
    private final ConfirmationTokenService confirmationTokenService ;
    private RegistrationEventService registrationEventService;


    public AthleteServiceImpl(TokenService tokenService, RefreshTokenService refreshTokenService, AthleteRepository athleteRepository, ConfirmationTokenService confirmationTokenService) {
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.athleteRepository = athleteRepository;
        this.confirmationTokenService = confirmationTokenService;
    }
    @Autowired
    @Lazy
    public void setRegistrationEventService(RegistrationEventService registrationEventService){
        this.registrationEventService = registrationEventService ;
    }

    @Override
    public Athlete saveAthlete(final Athlete athlete) {
        return athleteRepository.save(athlete);
    }

    @Override
    public Athlete getAthleteById(final Long athleteId) {
        return athleteRepository.findById(athleteId)
                .orElseThrow(()-> new ResourceNotFoundCustomException("athlete not found"));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> deleteAthleteById(final Long athleteId) {
        try {

            final Athlete athlete = getAthleteById(athleteId) ;
            //delete User references
            tokenService.deleteByUserId(athleteId);
            refreshTokenService.deleteTokenByUserId(athleteId);
            confirmationTokenService.deleteConfirmTokenByUserId(athleteId);
            registrationEventService.deleteAthleteFromAllEvents(athleteId);


            //delete User
            athleteRepository.deleteById(athleteId);
            String successResponse = String.format("athlete with email '%s' has been successfully deleted.", athlete.getEmail()) ;
            return new ResponseEntity<>(successResponse, HttpStatus.OK);

        }catch (DataIntegrityViolationException | ConstraintViolationException  e){
            throw new DatabaseCustomException("Cannot delete an athlete as it is referenced by other records") ;
        }
    }

    @Override
    public ResponseEntity<Object> getAllAthletes() {
        List<Athlete> athletes = athleteRepository.findAll() ;
        if(athletes.isEmpty()) {throw new ResourceNotFoundCustomException("No athletes found") ;}

        return new ResponseEntity<>(athletes , HttpStatus.OK) ;
    }
}
