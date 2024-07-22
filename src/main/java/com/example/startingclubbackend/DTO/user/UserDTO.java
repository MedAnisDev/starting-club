package com.example.startingclubbackend.DTO.user;

import com.example.startingclubbackend.model.role.Role;

public record UserDTO(Long id ,
                      String firstname ,
                      String lastname ,
                      String email ,
                      String password ,
                      boolean isEnabled ,
                      String licenceId,
                      Role role
                      ) {
}
