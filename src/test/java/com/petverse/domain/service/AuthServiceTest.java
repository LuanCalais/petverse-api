package com.petverse.domain.service;

import com.petverse.domain.dto.AuthResponseDTO;
import com.petverse.domain.dto.LoginRequestDTO;
import com.petverse.domain.dto.RegisterRequestDTO;
import com.petverse.domain.dto.UserCreateDTO;
import com.petverse.domain.entity.Pet;
import com.petverse.domain.entity.Post;
import com.petverse.domain.entity.User;
import com.petverse.exception.BusinessException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("AuthService Unit Tests")
public class AuthServiceTest {
    @Inject
    AuthService authService;

    @Inject
    UserService userService;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        Post.deleteAll();
        Pet.deleteAll();
        User.deleteAll();
    }

    @Test
    @DisplayName("Should registry successfully")
    void shouldRegistrySuccessfully() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.name = "Fulano de tal";
        dto.email = "fulanoDeTal@email.com";
        dto.password = "senha123";
        dto.phone = "+5511999999999";

        AuthResponseDTO response = authService.register(dto);

        assertNotNull(response.token);
        assertNotNull(response.user.id);
        assertTrue(response.user.active);
        assertEquals(response.user.name, dto.name);
        assertEquals(response.user.email, dto.email.toLowerCase());
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

        RegisterRequestDTO dto2 = new RegisterRequestDTO();
        dto2.name = "Fulano de tal 2";
        dto2.email = "fulanoDeTal@email.com";
        dto2.password = "senha1234";
        dto2.phone = "+5512999999999";

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.register(dto2);
        });

        assertEquals("User already exists with this e-mail", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when user is invalid")
    void shouldThrowExceptionWhenUserIsInvalid() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.email = "fulanoDaSilve@email.com";
        dto.password = "1234578senha";
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(dto);
        });

        assertEquals("Invalid user", exception.getMessage());
    }

    @Test
    @DisplayName("Should login user successfully")
    void shouldLoginSuccessfully() {
        RegisterRequestDTO registerDTO = new RegisterRequestDTO();
        registerDTO.name = "Fulano de tal";
        registerDTO.email = "fulanoDeTal@email.com";
        registerDTO.password = "senha123";
        registerDTO.phone = "+5511999999999";
        authService.register(registerDTO);

        LoginRequestDTO loginDTO = new LoginRequestDTO();
        loginDTO.email = "fulanoDeTal@email.com";
        loginDTO.password = "senha123";

        AuthResponseDTO response = authService.login(loginDTO);

        assertNotNull(response.token);
        assertNotNull(response.user.id);
        assertTrue(response.user.active);
        assertEquals(registerDTO.name, response.user.name);
        assertEquals(registerDTO.email.toLowerCase(), response.user.email);
    }

    @Test
    @DisplayName("Should throw exception if User is invalid")
    void shouldThrowExceptionIfUserIsInvalid() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.name = "Fulano de tal";
        dto.email = "fulanoDeTal@email.com";
        dto.password = "senha123";
        dto.phone = "+5511999999999";

        authService.register(dto);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.me("sicranoDeTal@email.com");
        });

        assertEquals("User not found", exception.getMessage());
    }
}
