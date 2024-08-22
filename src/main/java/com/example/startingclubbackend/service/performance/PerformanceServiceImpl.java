package com.example.startingclubbackend.service.performance;

import com.example.startingclubbackend.DTO.performance.PerformanceDTO;
import com.example.startingclubbackend.DTO.performance.PerformanceDTOMapper;
import com.example.startingclubbackend.exceptions.custom.DatabaseCustomException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.performance.Performance;
import com.example.startingclubbackend.model.user.admin.Admin;
import com.example.startingclubbackend.model.user.athlete.Athlete;
import com.example.startingclubbackend.repository.PerformanceRepository;
import com.example.startingclubbackend.service.athlete.AthleteService;
import jakarta.validation.constraints.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PerformanceServiceImpl implements PerformanceService{
    private final PerformanceRepository performanceRepository ;
    private final AthleteService athleteService ;
    private final PerformanceDTOMapper performanceDTOMapper ;

    public PerformanceServiceImpl(PerformanceRepository performanceRepository, AthleteService athleteService, PerformanceDTOMapper performanceDTOMapper) {
        this.performanceRepository = performanceRepository;
        this.athleteService = athleteService;
        this.performanceDTOMapper = performanceDTOMapper;
    }

    @Override
    public ResponseEntity<Object> createPerformance(@NotNull final PerformanceDTO performanceDTO , final Long athleteId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
        Admin currentAdmin = (Admin) auth.getPrincipal() ;
        Athlete currAthlete = athleteService.getAthleteById(athleteId) ;

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
    public ResponseEntity<Object> updatePerformance(@NotNull final PerformanceDTO performanceDTO ,final Long performanceId) {
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
                .orElseThrow(()-> new ResourceNotFoundCustomException("performance required not found")) ;
        final PerformanceDTO performanceDTO = performanceDTOMapper.apply(currPerformance) ;
        return new ResponseEntity<>(performanceDTO ,HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<Object> deletePerformanceById(Long performanceId) {
        performanceRepository.deleteById(performanceId);
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
