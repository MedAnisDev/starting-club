package com.example.startingclubbackend.service.performance;

import com.example.startingclubbackend.DTO.performance.PerformanceDTO;
import com.example.startingclubbackend.DTO.performance.PerformanceDTOMapper;
import com.example.startingclubbackend.exceptions.custom.DatabaseCustomException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.performance.Performance;
import com.example.startingclubbackend.model.user.admin.Admin;
import com.example.startingclubbackend.model.user.athlete.Athlete;
import com.example.startingclubbackend.repository.AthleteRepository;
import com.example.startingclubbackend.repository.PerformanceRepository;
import com.example.startingclubbackend.repository.TrainingSessionRepository;
import com.example.startingclubbackend.service.athlete.AthleteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PerformanceServiceImpl implements PerformanceService{
    private final PerformanceRepository performanceRepository ;

    private final AthleteRepository athleteRepository ;
    private final PerformanceDTOMapper performanceDTOMapper ;
    private final TrainingSessionRepository trainingSessionRepository ;

    public PerformanceServiceImpl(PerformanceRepository performanceRepository, AthleteRepository athleteRepository, PerformanceDTOMapper performanceDTOMapper, TrainingSessionRepository trainingSessionRepository) {
        this.performanceRepository = performanceRepository;
        this.athleteRepository = athleteRepository;
        this.performanceDTOMapper = performanceDTOMapper;
        this.trainingSessionRepository = trainingSessionRepository;
    }

    @Override
    public ResponseEntity<Object> createPerformance(@NotNull @Valid final PerformanceDTO performanceDTO , final Long athleteId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
        Admin currentAdmin = (Admin) auth.getPrincipal() ;
        Athlete currAthlete = athleteRepository.findById(athleteId)
                .orElseThrow(() -> new ResourceNotFoundCustomException("athlete not found"));

        //build performance object
        final Performance currPerformance = new Performance() ;

        currPerformance.setFederationNote(performanceDTO.getFederationNote());
        currPerformance.setUpdatedAT(LocalDateTime.now());
        currPerformance.setCreatedBy(currentAdmin);
        currPerformance.setAthlete(currAthlete);

        savePerformance(currPerformance);
        return new ResponseEntity<>(performanceDTOMapper.apply(currPerformance) , HttpStatus.CREATED) ;
    }

    @Override
    public Performance getPerformanceById(final Long performanceId) {
        return performanceRepository.fetchByPerformanceId(performanceId)
                .orElseThrow(()-> new ResourceNotFoundCustomException("performance required not found")) ;
    }

    @Override
    public ResponseEntity<Object> updatePerformance(@NotNull @Valid final PerformanceDTO performanceDTO ,final Long performanceId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
        Admin currentAdmin = (Admin) auth.getPrincipal() ;

        //build performance object
        final Performance currPerformance = getPerformanceById(performanceId);
        currPerformance.setUpdatedBy(currentAdmin);
        currPerformance.setUpdatedAT(LocalDateTime.now());
        currPerformance.setFederationNote(performanceDTO.getFederationNote());

        savePerformance(currPerformance);
        return new ResponseEntity<>(performanceDTOMapper.apply(currPerformance) , HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<Object> getPerformanceByAthleteId(final Long athleteId) {
        final Performance currPerformance = performanceRepository.fetchByAthleteId(athleteId)
                .orElse(null) ;
        if(currPerformance !=null) {
            final PerformanceDTO performanceDTO = performanceDTOMapper.apply(currPerformance);
            return new ResponseEntity<>(performanceDTO, HttpStatus.OK);
        }
        else{
            return null ;
        }
    }

    @Transactional
    @Override
    public ResponseEntity<Object> deletePerformanceById(final Long performanceId) {
        //delete relations
        trainingSessionRepository.deleteAllByPerformanceId(performanceId);

        performanceRepository.deletePerformanceById(performanceId);
        return new ResponseEntity<>("performance requested is deleted successfully", HttpStatus.OK) ;
    }

    private void savePerformance(@NotNull final Performance currPerformance) {
        try{
            performanceRepository.save(currPerformance) ;
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseCustomException("error with saving this performance");
        }
    }
}
