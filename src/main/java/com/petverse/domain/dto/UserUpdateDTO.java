package com.petverse.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    public String name;

    @Email(message = "Email must be valid")
    public String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    public String password;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$|^$", message = "Phone must be valid")
    public String phone;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    public String bio;

    public String profileImageUrl;
}
