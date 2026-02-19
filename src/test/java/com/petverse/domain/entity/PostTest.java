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

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Post Entity Unit Test")
class PostTest {

    private Validator validator;
    private User owner;
    private Pet pet;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        owner = new User();
        owner.name = "Fulano de Tal";
        owner.email = "fulanoDeTal@example.com";
        owner.password = "senha12345";
        owner.bio = "Amo gatos";

        pet = new Pet();
        pet.name = "Pedro o Gato da Silva";
        pet.species = PetSpecies.CAT;
        pet.gender = PetGender.MALE;
        pet.owner = owner;
    }

    Post createBasicPost() {
        Post post = new Post();
        post.content = "Conteúdo postagem simples";
        post.owner = owner;
        post.pets = List.of(pet);
        return post;
    }

    @Test
    @DisplayName("Should create a valid post")
    void createPost() {
        Post post = createBasicPost();
        Set<ConstraintViolation<Post>> violations = validator.validate(post);
        assertTrue(violations.isEmpty(), "Post should be valid but got: " + violations);
    }

    @Test
    @DisplayName("Should fail when owner is null")
    void shouldFailWhenOwnerIsNull() {
        Post post = createBasicPost();
        post.owner = null;

        Set<ConstraintViolation<Post>> violations = validator.validate(post);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("owner")));
    }

    @Test
    @DisplayName("Should allow multiple pets in a post")
    void shouldAllowMultiplePets() {
        Pet anotherPet = new Pet();
        anotherPet.name = "Luna";
        anotherPet.species = PetSpecies.DOG;
        anotherPet.gender = PetGender.FEMALE;
        anotherPet.owner = owner;

        Post post = createBasicPost();
        post.pets = List.of(pet, anotherPet);

        Set<ConstraintViolation<Post>> violations = validator.validate(post);

        assertTrue(violations.isEmpty());
    }
}
