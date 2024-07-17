package com.example.startingclubbackend.DTO.user;

import com.example.startingclubbackend.model.User;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getFirstname(),
                user.getLastName(),
                user.getEmail(),
                user.isEnabled(),
                user.getRole());
    }
}
