package com.petverse.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    public String email;

    @NotBlank(message = "Password is required")
    public String password;

}
