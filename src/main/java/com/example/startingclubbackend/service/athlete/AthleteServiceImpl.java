package com.example.startingclubbackend.service.athlete;

import com.example.startingclubbackend.DTO.athlete.AthleteDTO;
import com.example.startingclubbackend.DTO.athlete.AthleteDTOMapper;
import com.example.startingclubbackend.exceptions.custom.DatabaseCustomException;
import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.file.FileRecord;
import com.example.startingclubbackend.model.user.athlete.Athlete;
import com.example.startingclubbackend.model.user.athlete.AthleteBranch;
import com.example.startingclubbackend.repository.AthleteRepository;
import com.example.startingclubbackend.repository.EventPerformanceRepository;
import com.example.startingclubbackend.repository.EventRepository;
import com.example.startingclubbackend.service.Token.ConfirmationTokenService;
import com.example.startingclubbackend.service.Token.RefreshTokenService;
import com.example.startingclubbackend.service.Token.TokenService;
import com.example.startingclubbackend.service.event.EventPerformanceService;
import com.example.startingclubbackend.service.event.RegistrationEventService;
import com.example.startingclubbackend.service.file.FileService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AthleteServiceImpl implements AthleteService {
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    private final AthleteRepository athleteRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private RegistrationEventService registrationEventService;

    private final AthleteDTOMapper athleteDTOMapper;
    private final FileService fileService ;
    private final PasswordEncoder passwordEncoder;
    private final EventPerformanceRepository eventPerformanceRepository ;
    @PersistenceContext
    private EntityManager entityManager;

    public AthleteServiceImpl(TokenService tokenService, RefreshTokenService refreshTokenService, AthleteRepository athleteRepository, ConfirmationTokenService confirmationTokenService, AthleteDTOMapper athleteDTOMapper, FileService fileService, PasswordEncoder passwordEncoder, EventPerformanceRepository eventPerformanceRepository) {
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.athleteRepository = athleteRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.athleteDTOMapper = athleteDTOMapper;
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;
        this.eventPerformanceRepository = eventPerformanceRepository;
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
    public ResponseEntity<Object> deleteAthleteById(final Long athleteId) throws IOException{
        try {

            final Athlete athlete = getAthleteById(athleteId);
            //delete User references
            tokenService.deleteByUserId(athleteId);
            refreshTokenService.deleteTokenByUserId(athleteId);
            confirmationTokenService.deleteConfirmTokenByUserId(athleteId);
            registrationEventService.deleteAthleteFromAllEvents(athleteId);
            for(FileRecord file : athlete.getFiles()){
                log.info("file deleted" + file.getPath() + "name "+file.getName());
                Path filePath = Paths.get(file.getPath());
                Files.delete(filePath);
            }
            eventPerformanceRepository.deleteAllByAthleteId(athleteId);

            //delete User
            athleteRepository.deleteById(athleteId);
            String successResponse = String.format("athlete with email '%s' has been successfully deleted.", athlete.getEmail());
            return new ResponseEntity<>(successResponse, HttpStatus.OK);

        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new DatabaseCustomException("Cannot delete an athlete as it is referenced by other records");
        }
    }

    @Override
    public ResponseEntity<Object> getAllAthletes(final long pageNumber , final String sortingColumn) {

        Sort sort = Sort.by(Sort.Order.desc(sortingColumn).nullsLast()) ;
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
    public ResponseEntity<Object> getAllCustomAthletes(final List<String> checkedColumns) {
        //building jpql query
        String athleteColumns = checkedColumns.stream()
                .map(col -> "a." + col + " as " + col)
                .collect(Collectors.joining(","));

        String jpql = "Select new map(" + athleteColumns + ") from Athlete a";
        Query query = entityManager.createQuery(jpql) ;

        //executing jpql query
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResult =(List<Map<String, Object>>) query.getResultList();
        return new ResponseEntity<>(queryResult , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> uploadFilesToAthlete(final Long athleteId, final List<MultipartFile> files) throws IOException {
        Athlete currentAthlete = getAthleteById(athleteId) ;

        for(MultipartFile file : files){
            final FileRecord currFile = fileService.handleFile(file);
            currFile.setAthlete(currentAthlete);
            fileService.saveFile(currFile);
        }
        return new ResponseEntity<>("files added to this athlete successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getCustomAthleteById(final Long athleteId) {
        final Athlete athlete =getAthleteById(athleteId) ;
        final AthleteDTO athleteDTO = athleteDTOMapper.apply(athlete) ;
        return new ResponseEntity<>(athleteDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateAthlete(final Long athleteId,@NotNull final  AthleteDTO athleteDTO) {
        final Athlete currAthlete = getAthleteById(athleteId) ;

        //building updated athlete
        final AthleteBranch branch = AthleteBranch.valueOf(athleteDTO.getBranch()) ;

        currAthlete.setFirstname(athleteDTO.getFirstname());
        currAthlete.setLastname(athleteDTO.getLastname());
        currAthlete.setPassword(passwordEncoder.encode(athleteDTO.getPassword()));
        currAthlete.setPhoneNumber(athleteDTO.getPhoneNumber());
        currAthlete.setLicenceID(athleteDTO.getLicenceID());
        currAthlete.setDateOfBirth(athleteDTO.getDateOfBirth());
        currAthlete.setBranch(branch);
        currAthlete.setHasMedal(athleteDTO.isHasMedal());

        saveAthlete(currAthlete) ;
        return new ResponseEntity<>(athleteDTOMapper.apply(currAthlete), HttpStatus.OK);
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
