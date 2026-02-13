package com.petverse.domain.dto;

import com.petverse.domain.entity.User;

import java.time.LocalDateTime;

public class UserResponseDTO {
    public Long id;
    public String name;
    public String email;
    public String phone;
    public String bio;
    public String profileImageUrl;
    public Boolean active;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public UserResponseDTO() {}

    public UserResponseDTO(User user) {
        this.id = user.id;
        this.name = user.name;
        this.email = user.email;
        this.phone = user.phone;
        this.bio = user.bio;
        this.profileImageUrl = user.profileImageUrl;
        this.active = user.active;
        this.createdAt = user.createdAt;
        this.updatedAt = user.updatedAt;
    }
}
