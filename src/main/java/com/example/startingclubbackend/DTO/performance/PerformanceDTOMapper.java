package com.example.startingclubbackend.DTO.performance;

import com.example.startingclubbackend.DTO.performance.trainingSession.TrainingSessionDTOMapper;
import com.example.startingclubbackend.DTO.user.UserPublicDTO;
import com.example.startingclubbackend.DTO.user.UserPublicDTOMapper;
import com.example.startingclubbackend.model.performance.Performance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.function.Function;

@Service
public class PerformanceDTOMapper implements Function<Performance, PerformanceDTO> {
    private final TrainingSessionDTOMapper trainingSessionDTOMapper ;
    private final UserPublicDTOMapper userPublicDTOMapper ;

    public PerformanceDTOMapper(TrainingSessionDTOMapper trainingSessionDTOMapper, UserPublicDTOMapper userPublicDTOMapper) {
        this.trainingSessionDTOMapper = trainingSessionDTOMapper;
        this.userPublicDTOMapper = userPublicDTOMapper;
    }

    @Override
    public PerformanceDTO apply(Performance performance) {
        return new PerformanceDTO(
                performance.getId() ,
                performance.getFederationNote(),
                performance.getCreatedAT() ,
                performance.getUpdatedAT() ,
                performance.getCreatedBy() !=null ? userPublicDTOMapper.apply(performance.getCreatedBy()) : null ,
                performance.getUpdatedBy() != null ? userPublicDTOMapper.apply(performance.getUpdatedBy()) : null ,
                performance.getTrainingSessionList() != null ? performance.getTrainingSessionList()
                        .stream()
                        .map(trainingSessionDTOMapper)
                        .toList() :new ArrayList<>()
        );
    }
}
