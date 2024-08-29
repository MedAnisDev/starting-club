package com.example.startingclubbackend.service.performance;

import com.example.startingclubbackend.DTO.performance.trainingSession.TrainingSessionDTO;
import com.example.startingclubbackend.DTO.performance.trainingSession.TrainingSessionDTOMapper;
import com.example.startingclubbackend.exceptions.custom.DatabaseCustomException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.performance.Performance;
import com.example.startingclubbackend.model.performance.TrainingSession;
import com.example.startingclubbackend.model.user.admin.Admin;
import com.example.startingclubbackend.repository.TrainingSessionRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class TrainingSessionServiceImpl implements TrainingSessionService{
    private final TrainingSessionRepository trainingSessionRepository ;
    private final TrainingSessionDTOMapper trainingSessionDTOMapper ;
    private final PerformanceService performanceService ;

    public TrainingSessionServiceImpl(TrainingSessionRepository trainingSessionRepository, TrainingSessionDTOMapper trainingSessionDTOMapper, PerformanceService performanceService) {
        this.trainingSessionRepository = trainingSessionRepository;
        this.trainingSessionDTOMapper = trainingSessionDTOMapper;
        this.performanceService = performanceService;
    }

    @Transactional
    @Override
    public ResponseEntity<Object> createTrainingSession(@NotNull @Valid  final TrainingSessionDTO trainingSessionDTO,
                                                        final Long performanceId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
        Admin currentAdmin = (Admin) auth.getPrincipal() ;
        Performance currPerformance = performanceService.getPerformanceById(performanceId) ;

        //building Training session
        TrainingSession currentSession = new TrainingSession() ;
        currentSession.setDate(trainingSessionDTO.getDate());
        currentSession.setSessionNote(trainingSessionDTO.getSessionNote());
        currentSession.setUpdatedAT(LocalDateTime.now());
        currentSession.setCreatedBy(currentAdmin);
        currentSession.setPerformance(currPerformance);

        saveTrainingSession(currentSession) ;

        return new ResponseEntity<>(trainingSessionDTOMapper.apply(currentSession) , HttpStatus.CREATED) ;
    }

    @Transactional
    @Override
    public ResponseEntity<Object> updateTrainingSession(final Long sessionId,@NotNull @Valid final TrainingSessionDTO trainingSessionDTO) {
        //get current session
        TrainingSession currentSession = getTrainingSessionById(sessionId);

        //get current admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
        Admin currentAdmin = (Admin) auth.getPrincipal() ;

        //building session
        currentSession.setDate(trainingSessionDTO.getDate());
        currentSession.setSessionNote(trainingSessionDTO.getSessionNote());
        currentSession.setUpdatedAT(LocalDateTime.now());
        currentSession.setUpdatedBy(currentAdmin);

        saveTrainingSession(currentSession);
        return new ResponseEntity<>(trainingSessionDTOMapper.apply(currentSession) , HttpStatus.CREATED) ;
    }

    @Override
    public ResponseEntity<Object> fetchAllTrainingSessions(final long pageNumber , final String sortedBY) {

        Sort sort = Sort.by(Sort.Order.desc(sortedBY).nullsLast()) ;
        Pageable pageable = PageRequest.of(
                (int)pageNumber - 1 ,
                5,
                sort
        );
        final List<TrainingSessionDTO>TrainingSessionDTOList = trainingSessionRepository.fetchAllTrainingSessions(pageable)
                .stream()
                .map(trainingSessionDTOMapper)
                .toList() ;
        return new ResponseEntity<>(TrainingSessionDTOList , HttpStatus.OK) ;
    }

    @Transactional
    @Override
    public ResponseEntity<Object> deleteTrainingSessionById(final Long sessionId) {

        trainingSessionRepository.deleteTrainingSessionById(sessionId);

        final String successResponse = String.format("Training Session with ID : %d deleted successfully", sessionId);
        return new ResponseEntity<>(successResponse , HttpStatus.OK);
    }

    @Transactional
    @Override
    public void deleteAllSessionByPerformanceId(final Long performanceId) {
        trainingSessionRepository.deleteAllByPerformanceId(performanceId);
    }


    private void saveTrainingSession(@NotNull final TrainingSession currentSession) {
        try{
            trainingSessionRepository.save(currentSession) ;
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseCustomException("error with saving this session");
        }
    }

    public TrainingSession getTrainingSessionById(final Long sessionId){
        return trainingSessionRepository.fetchById(sessionId)
                .orElseThrow(()-> new ResourceNotFoundCustomException("training session requested not found")) ;
    }
}
