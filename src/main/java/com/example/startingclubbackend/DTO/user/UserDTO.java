package com.example.startingclubbackend.DTO.user;

import com.example.startingclubbackend.model.Role;

public record UserDTO(String firstname ,
                      String lastname ,
                      String email ,
                      boolean isEnabled ,
                      Role role
                      ) {
}
