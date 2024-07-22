package com.example.startingclubbackend.DTO.user;

import com.example.startingclubbackend.model.user.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.getLicenceID(),
                user.getRole());
    }
}
