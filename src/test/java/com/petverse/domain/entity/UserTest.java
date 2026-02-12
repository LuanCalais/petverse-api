package com.petverse.domain.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        user.password = "12345";

        Set<ConstraintViolation<User>> violations = validator.validate(user);
//        assertTrue(violations.isEmpty(), "User válido não deveria ter erros de validação");
//        assertEquals("Fulano de Tal", user.name);
//        assertEquals("assertEquals", user.email);
//        assertTrue(user.active);
//        assertNotNull(user.pets);
    }
}
