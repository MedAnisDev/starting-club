package com.example.startingclubbackend.DTO.user;

import com.example.startingclubbackend.model.role.Role;

public record UserPublicDTO(
        Long id,
        String firstname,
        String lastname,
        Role role) {
}

