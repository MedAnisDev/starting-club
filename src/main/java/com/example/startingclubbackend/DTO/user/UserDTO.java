package com.example.startingclubbackend.DTO.user;

import com.example.startingclubbackend.model.role.Role;

import java.time.LocalDateTime;

public record UserDTO(Long id ,
                      String firstname ,
                      String lastname ,
                      String email ,
                      boolean isEnable ,
                      LocalDateTime createdAt,
                      Role role
                      ) {
}
