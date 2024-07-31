package com.example.startingclubbackend.DTO.event;

import com.example.startingclubbackend.DTO.user.UserDTOMapper;
import com.example.startingclubbackend.DTO.user.UserPublicDTOMapper;
import com.example.startingclubbackend.model.event.Event;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EventDTOMapper  implements Function<Event ,EventDTO> {
    private final UserPublicDTOMapper userPublicDTOMapper;

    public EventDTOMapper(UserPublicDTOMapper userPublicDTOMapper) {

        this.userPublicDTOMapper = userPublicDTOMapper;
    }

    @Override
    public EventDTO apply(Event event) {
        return new EventDTO(
                event.getId() ,
                event.getTitle(),
                event.getLocation(),
                event.getDescription(),
                event.getDate(),
                userPublicDTOMapper.apply(event.getCreated_by())
        );
    }
}
