package com.petverse.domain.service;

import com.petverse.domain.dto.UserCreateDTO;
import com.petverse.domain.dto.UserResponseDTO;
import com.petverse.domain.dto.UserUpdateDTO;
import com.petverse.domain.entity.User;
import com.petverse.exception.BusinessException;
import com.petverse.exception.ResourceNotFoundException;
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
            throw new BusinessException("User already exists with this e-mail");
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

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {
        User user = User.findById(id);
        if (user == null) {
            throw new BusinessException("User not found with id: " + id);
        }

        if (dto.email != null && !dto.email.equals(user.email)){
            User existedUser = User.findByEmail(dto.email);
            if (existedUser != null) {
                throw new BusinessException("Email already in use");
            }
            user.email = dto.email;
        }

        if(dto.name != null) user.name = dto.name;
        if(dto.password != null) user.password = dto.password;
        if(dto.phone != null) user.phone = dto.phone;
        if (dto.bio != null) user.bio = dto.bio;
        if (dto.profileImageUrl != null) user.profileImageUrl = dto.profileImageUrl;

        return new UserResponseDTO(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = User.findById(id);
        if (user == null) {
            throw new BusinessException("User not found with id: " + id);
        }
        user.active = false;
    }

    public List<UserResponseDTO> listAll() {
        return User.listActive()
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    public UserResponseDTO findById(Long id) {
        User user = User.findById(id);
        if (user == null || !user.active) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        return new UserResponseDTO(user);
    }

    public UserResponseDTO findByEmail(String email) {
        User user = User.findByEmail(email);
        if (user == null) {
            throw new BusinessException("User not found with email: " + email);
        }
        return new UserResponseDTO(user);
    }
}
