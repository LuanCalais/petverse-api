package com.petverse.domain.entity;

import com.petverse.domain.enums.PetGender;
import com.petverse.domain.enums.PetSpecies;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

@DisplayName("Pet Entity Unit Tests")
class PetTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    User createUser() {
        User user = new User();
        user.name = "Fulano de Tal";
        user.email = "fulanoDeTal@example.com";
        user.password = "senha12345";
        return user;
    }

    @Test
    @DisplayName("Should create a valid pet with required fields")
    void shouldCreateValidPet() {
        final User owner = createUser();

        Pet pet = new Pet();
        pet.name = "Colin Firth";
        pet.gender = PetGender.MALE;
        pet.species = PetSpecies.DOG;
        pet.owner = owner;

        Set<ConstraintViolation<Pet>> violations = validator.validate(pet);

        assertTrue(violations.isEmpty(), "Pet válido não deveria ter erros de validação");
        assertEquals("Colin Firth", pet.name);
        assertEquals(PetGender.MALE, pet.gender);
        assertEquals(PetSpecies.DOG, pet.species);
        assertEquals(owner, pet.owner);
    }

    @Test
    @DisplayName("Should fail when name is blank")
    void shouldFailWhenNameIsBlank() {
        User owner = createUser();
        Pet pet = new Pet();
        pet.name = "";
        pet.gender = PetGender.MALE;
        pet.species = PetSpecies.DOG;
        pet.owner = owner;

        Set<ConstraintViolation<Pet>> violations = validator.validate(pet);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Pet name is required")));
    }

    @Test
    @DisplayName("Should fail when owner is null")
    void shouldFailWhenOwnerIsNull() {
        Pet pet = new Pet();
        pet.name = "Colin Firth";
        pet.gender = PetGender.MALE;
        pet.species = PetSpecies.DOG;

        Set<ConstraintViolation<Pet>> violations = validator.validate(pet);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Owner is required")));
    }

}
