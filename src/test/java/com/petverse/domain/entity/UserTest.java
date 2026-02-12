package com.petverse.domain.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

@DisplayName("User Entity Unit Tests")
class UserTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should create a valid user with required fields")
    void shouldCreateValidUser() {
        User user = new User();
        user.name = "Fulano de Tal";
        user.email = "fulanoDeTal@example.com";
        user.password = "senha12345";

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "User válido não deveria ter erros de validação");
        assertEquals("Fulano de Tal", user.name);
        assertEquals("fulanoDeTal@example.com", user.email);
        assertTrue(user.active);
        assertNotNull(user.pets);
    }

    @Test
    @DisplayName("Should create user with optional fields")
    void shouldCreateValidUserWithOptionalFields() {
        User user = new User();
        user.name = "Fulano de Tal";
        user.email = "fulanoDeTal@example.com";
        user.password = "senha12345";
        user.phone = "+5511999999999";
        user.bio = "Amante de cachorros e café";
        user.profileImageUrl = "https://example.com/photo.jpg";

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
        assertEquals("+5511999999999", user.phone);
        assertEquals("Amante de cachorros e café", user.bio);
    }
}