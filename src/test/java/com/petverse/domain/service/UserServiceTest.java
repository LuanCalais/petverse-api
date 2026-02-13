package com.petverse.domain.service;

import com.petverse.domain.dto.UserCreateDTO;
import com.petverse.domain.dto.UserResponseDTO;
import com.petverse.domain.dto.UserUpdateDTO;
import com.petverse.domain.entity.User;
import com.petverse.exception.BusinessException;
import com.petverse.exception.ResourceNotFoundException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("UserService Unit Tests")
public class UserServiceTest {
    @Inject
    UserService userService;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        User.deleteAll();
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.name = "Fulano de tal";
        dto.email = "fulanoDeTal@email.com";
        dto.password = "senha123";
        dto.phone = "+5511999999999";
        dto.bio = "Amante de cachorros";

        UserResponseDTO response = userService.create(dto);

        assertNotNull(response.bio);
        assertEquals(dto.name, response.name);
        assertEquals(dto.email, response.email);
        assertTrue(response.active);
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.name = "Fulano de tal";
        dto.email = "fulanoDeTal@email.com";
        dto.password = "senha123";
        dto.bio = "Amante de cachorros";
        userService.create(dto);

        UserCreateDTO dto2 = new UserCreateDTO();
        dto2.name = "Maria";
        dto2.email = dto.email;
        dto2.password = "senha456";
        dto2.bio = "Amante de cachorros";

        BusinessException exception = assertThrows(BusinessException.class, () -> {
           userService.create(dto2);
        });

        assertEquals("User already exists with this e-mail", exception.getMessage());
    }

    @Test
    @DisplayName("Should find user by ID")
    void shouldFindUserById() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.name = "Fulano de tal";
        dto.email = "fulanoDeTal@email.com";
        dto.password = "senha123";
        dto.bio = "Amante de cachorros";

        UserResponseDTO created = userService.create(dto);
        UserResponseDTO found = userService.findById(created.id);

        assertEquals(created.id, found.id);
        assertEquals(dto.name, found.name);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(999L);
        });

        assertEquals("User not found with id: 999", exception.getMessage());
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUser() {
        UserCreateDTO createDto = new UserCreateDTO();
        createDto.name = "Fulano de tal";
        createDto.email = "fulanoDeTal@email.com";
        createDto.password = "senha123";
        createDto.bio = "Amante de cachorros";
        UserResponseDTO created = userService.create(createDto);

        UserUpdateDTO updateDto = new UserUpdateDTO();
        updateDto.name = "Fulano de tal [EDITADO]";
        updateDto.email = "fulanoEditadoDeTal@email.com";
        updateDto.bio = "Amante de cachorros [EDITADO]";
        UserResponseDTO updated = userService.update(created.id, updateDto);

        assertEquals(updateDto.name, updated.name);
        assertEquals(updateDto.email, updated.email);
        assertEquals(updateDto.bio, updated.bio);
    }

    @Test
    @DisplayName("Should delete user (soft delete)")
    void shouldSoftDeleteUser() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.name = "Fulano de tal";
        dto.email = "fulanoDeTal@email.com";
        dto.password = "senha123";
        dto.bio = "Amante de cachorros";

        UserResponseDTO created = userService.create(dto);

        userService.delete(created.id);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(created.id);
        });

        assertEquals("User not found with id: " + created.id, exception.getMessage());    }
}
