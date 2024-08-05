package com.example.startingclubbackend.DTO.auth;

import com.example.startingclubbackend.DTO.user.UserDTO;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class RegisterResponseDTO {

    private UserDTO userDTO ;
    private String refreshToken;
    private String confirmationToken;

    public RegisterResponseDTO(UserDTO userDTO , String refreshToken , String confirmationToken){
        this.userDTO =userDTO ;
        this.refreshToken =refreshToken ;
        this.confirmationToken = confirmationToken ;
    }
}
