package com.example.startingclubbackend.DTO.auth;

import com.example.startingclubbackend.DTO.user.UserDTO;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class RegisterResponseDTO {

    private UserDTO userDTO ;
    private String token ;
    public RegisterResponseDTO(UserDTO userDTO , String token){
        this.userDTO =userDTO ;
        this.token = token ;
    }
}
