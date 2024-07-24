package com.example.startingclubbackend.service.athlete;

import com.example.startingclubbackend.model.user.Athlete;
import com.example.startingclubbackend.repository.AthleteRepository;
import org.springframework.stereotype.Service;

@Service
public class AthleteServiceImpl implements AthleteService{
    private final AthleteRepository athleteRepository ;

    public AthleteServiceImpl(AthleteRepository athleteRepository) {
        this.athleteRepository = athleteRepository;
    }

    @Override
    public Athlete saveAthlete(final Athlete athlete) {
        return athleteRepository.save(athlete);
    }
}
