package com.example.startingclubbackend.DTO.user;

import com.example.startingclubbackend.model.role.Role;

import java.time.LocalDateTime;

public record UserDTO(Long id ,
                      String firstname ,
                      String lastname ,
                      String email ,
                      String password ,
                      boolean isEnabled ,
                      LocalDateTime createdAt,
                      Role role
                      ) {
}
