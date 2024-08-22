package com.example.startingclubbackend.DTO.performance.trainingSession;

import com.example.startingclubbackend.DTO.performance.trainingSession.TrainingSessionDTO;
import com.example.startingclubbackend.DTO.user.UserPublicDTOMapper;
import com.example.startingclubbackend.model.performance.TrainingSession;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TrainingSessionDTOMapper implements Function<TrainingSession , TrainingSessionDTO> {
    private final UserPublicDTOMapper userPublicDTOMapper ;

    public TrainingSessionDTOMapper(UserPublicDTOMapper userPublicDTOMapper) {
        this.userPublicDTOMapper = userPublicDTOMapper;
    }

    @Override
    public TrainingSessionDTO apply(TrainingSession trainingSession) {
        return new TrainingSessionDTO(
                trainingSession.getId() ,
                trainingSession.getDate() ,
                trainingSession.getSessionNote(),
               trainingSession.getCreatedAT() ,
               trainingSession.getUpdatedAT() ,
               trainingSession.getCreatedBy() !=null ? userPublicDTOMapper.apply(trainingSession.getCreatedBy()) : null ,
               trainingSession.getUpdatedBy() != null ? userPublicDTOMapper.apply(trainingSession.getUpdatedBy()) : null
        );
    }
}
