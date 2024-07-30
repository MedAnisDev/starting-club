package com.example.startingclubbackend.DTO.user;

import com.example.startingclubbackend.model.user.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserPublicDTOMapper implements Function<User,UserPublicDTO> {

    @Override
    public UserPublicDTO apply(User user) {
        return new UserPublicDTO(
                user.getId() ,
                user.getFirstname(),
                user.getLastname(),
                user.getRole()
        );
    }
}
