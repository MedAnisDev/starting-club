package com.example.startingclubbackend.service.athlete;

import com.example.startingclubbackend.DTO.athlete.AthleteDTO;
import com.example.startingclubbackend.DTO.athlete.AthleteDTOMapper;
import com.example.startingclubbackend.exceptions.custom.DatabaseCustomException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.user.Athlete;
import com.example.startingclubbackend.repository.AthleteRepository;
import com.example.startingclubbackend.service.Token.ConfirmationTokenService;
import com.example.startingclubbackend.service.Token.RefreshTokenService;
import com.example.startingclubbackend.service.Token.TokenService;
import com.example.startingclubbackend.service.event.RegistrationEventService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AthleteServiceImpl implements AthleteService {
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    private final AthleteRepository athleteRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private RegistrationEventService registrationEventService;

    private final AthleteDTOMapper athleteDTOMapper;
    @PersistenceContext
    private EntityManager entityManager;

    public AthleteServiceImpl(TokenService tokenService, RefreshTokenService refreshTokenService, AthleteRepository athleteRepository, ConfirmationTokenService confirmationTokenService, AthleteDTOMapper athleteDTOMapper) {
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.athleteRepository = athleteRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.athleteDTOMapper = athleteDTOMapper;
    }

    @Autowired
    @Lazy
    public void setRegistrationEventService(RegistrationEventService registrationEventService) {
        this.registrationEventService = registrationEventService;
    }

    @Override
    public Athlete saveAthlete(final Athlete athlete) {
        return athleteRepository.save(athlete);
    }

    @Override
    public Athlete getAthleteById(final Long athleteId) {
        return athleteRepository.findById(athleteId)
                .orElseThrow(() -> new ResourceNotFoundCustomException("athlete not found"));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> deleteAthleteById(final Long athleteId) {
        try {

            final Athlete athlete = getAthleteById(athleteId);
            //delete User references
            tokenService.deleteByUserId(athleteId);
            refreshTokenService.deleteTokenByUserId(athleteId);
            confirmationTokenService.deleteConfirmTokenByUserId(athleteId);
            registrationEventService.deleteAthleteFromAllEvents(athleteId);


            //delete User
            athleteRepository.deleteById(athleteId);
            String successResponse = String.format("athlete with email '%s' has been successfully deleted.", athlete.getEmail());
            return new ResponseEntity<>(successResponse, HttpStatus.OK);

        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new DatabaseCustomException("Cannot delete an athlete as it is referenced by other records");
        }
    }

    @Override
    public ResponseEntity<Object> getAllAthletes(final long pageNumber , final String columnName) {

        Sort sort = Sort.by(Sort.Order.desc(columnName).nullsLast()) ;
        Pageable pageable = PageRequest.of(
                (int)pageNumber -1 ,
                5,
                sort
        );
        final List<Athlete> athletes = athleteRepository.fetchAllAthletes(pageable);
        if (athletes.isEmpty()) {
            throw new ResourceNotFoundCustomException("No athletes found");
        }

        final List<AthleteDTO> athletesDTOList = athletes.stream().map(athleteDTOMapper).toList();
        return new ResponseEntity<>(athletesDTOList, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> getAllCustomAthletes(List<String> checkedColumns) {
        String athleteColumns = checkedColumns.stream()
//                .filter(col -> !col.contains("."))
                .map(col -> "a." + col + " as " + col)
                .collect(Collectors.joining(","));

//        String eventColumns = checkedColumns.stream()
//                .filter(col -> col.contains("."))
//                .map(col -> "e." + col.split("[.]",2)[1] + " as " + col.split("[.]",2)[1])
//                .collect(Collectors.joining(","));

//        String columns = athleteColumns ;
//        if(!eventColumns.isEmpty()){
//            columns += "," + eventColumns;
//        }

        String jpql = "Select new map(" + athleteColumns + ") from Athlete a LEFT JOIN a.registeredEvents e";
        Query query = entityManager.createQuery(jpql) ;

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResult =(List<Map<String, Object>>) query.getResultList();
        return new ResponseEntity<>(queryResult , HttpStatus.OK);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateAge(){
        List<Athlete> athletes = athleteRepository.findAll() ;
        for(Athlete athlete : athletes){
            athlete.setAge(Period.between(athlete.getDateOfBirth() , LocalDate.now()).getYears());
            saveAthlete(athlete) ;
        }
    }

}
