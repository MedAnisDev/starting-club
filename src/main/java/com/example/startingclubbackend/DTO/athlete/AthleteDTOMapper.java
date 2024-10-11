package com.example.startingclubbackend.DTO.athlete;

import com.example.startingclubbackend.model.user.athlete.Athlete;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AthleteDTOMapper implements Function<Athlete , AthleteDTO> {
    @Override
    public AthleteDTO apply(Athlete athlete) {
        return new AthleteDTO(
                athlete.getId(),
                athlete.getFirstname(),
                athlete.getLastname(),
                athlete.getEmail(),
                athlete.getPassword(),
                athlete.getPhoneNumber() ,
                athlete.getLicenceID(),
                athlete.getDateOfBirth() ,
                athlete.getBranch().name() ,
                athlete.getAge(),
                athlete.getCreatedAt() ,
                athlete.getHasMedal() != null ? athlete.getHasMedal() : false
        );
    }
}
