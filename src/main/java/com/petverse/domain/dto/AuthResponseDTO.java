package com.petverse.domain.dto;

public class AuthResponseDTO {
    public String token;
    public UserAuthResponseDTO user;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String token, UserAuthResponseDTO user) {
        this.token = token;
        this.user = user;
    }
}
