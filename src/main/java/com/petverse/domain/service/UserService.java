package com.petverse.domain.service;

import com.petverse.domain.dto.UserCreateDTO;
import com.petverse.domain.dto.UserResponseDTO;
import com.petverse.domain.entity.User;
import com.petverse.exception.BusinessException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {
    @Transactional
    public UserResponseDTO create(UserCreateDTO dto) {
        User existingUser = User.findByEmail(dto.email);
        if (existingUser != null) {
            throw new BusinessException("User already exists");
        }

        User newUser = new User();
        newUser.name = dto.name;
        newUser.email = dto.email;
        newUser.password = dto.password;
        newUser.phone = dto.phone;
        newUser.bio = dto.bio;
        newUser.profileImageUrl = dto.profileImageUrl;

        newUser.persist();

        return new UserResponseDTO(newUser);
    }

    public List<UserResponseDTO> listAll() {
        return User.listActive()
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    public UserResponseDTO findByEmail(String email) {
        User user = User.findByEmail(email);
        if (user == null) {
            throw new BusinessException("User not found with email: " + email);
        }
        return new UserResponseDTO(user);
    }
}
