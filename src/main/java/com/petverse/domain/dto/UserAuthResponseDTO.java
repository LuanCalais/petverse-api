package com.petverse.domain.dto;

import com.petverse.domain.entity.User;

import java.time.LocalDateTime;

public class UserAuthResponseDTO {


    public Long id;
    public String name;
    public String email;
    public String phone;
    public String bio;
    public String profileImageUrl;
    public Boolean active;
    public LocalDateTime createdAt;

    public static UserAuthResponseDTO from(User user) {
        UserAuthResponseDTO dto = new UserAuthResponseDTO();
        dto.id = user.id;
        dto.name = user.name;
        dto.email = user.email;
        dto.phone = user.phone;
        dto.bio = user.bio;
        dto.profileImageUrl = user.profileImageUrl;
        dto.active = user.active;
        dto.createdAt = user.createdAt;
        return dto;
    }
}
