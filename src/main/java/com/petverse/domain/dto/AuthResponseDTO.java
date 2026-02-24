package com.petverse.domain.dto;

public class AuthResponseDTO {
    public String token;
    public UserAuthRespondeDTO user;

    public AuthResponseDTO(String token, UserAuthRespondeDTO user) {
        this.token = token;
        this.user = user;
    }
}
