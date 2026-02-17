package com.petverse.domain.service;

import com.petverse.domain.dto.PetCreateDTO;
import com.petverse.domain.dto.PetResponseDTO;
import com.petverse.domain.entity.Pet;
import com.petverse.domain.entity.User;
import com.petverse.domain.enums.PetGender;
import com.petverse.domain.enums.PetSize;
import com.petverse.domain.enums.PetSpecies;
import com.petverse.exception.ResourceNotFoundException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("PetSevice Unit Tests")
public class PetServiceTest {
    @Inject
    PetService petService;

    private User ownerTest;
    private Long ownerId;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        Pet.deleteAll();
        User.deleteAll();

        ownerTest = new User();
        ownerTest.name = "João Silva";
        ownerTest.email = "joao@test.com";
        ownerTest.password = "senha12345";
        ownerTest.bio = "Amante de pets";
        ownerTest.persist();

        ownerId = ownerTest.id;
    }

    @Test
    @DisplayName("Should create pet successfully")
    void shouldCreatePetSuccessfully() {
        PetCreateDTO dto = new PetCreateDTO();
        dto.name = "Pedro o gato";
        dto.ownerId = ownerId;
        dto.species = PetSpecies.CAT;
        dto.birthDate = LocalDate.parse("2020-05-15");
        dto.breed = "Fofurinho";
        dto.gender = PetGender.MALE;
        dto.size = PetSize.EXTRA_LARGE;
        dto.weight = 32.5;
        dto.bio = "Sou um cachorro muito fofo e brincalhão";
        dto.profileImageUrl = "https://example.com/photos/rex.jpg";
        dto.microchipNumber = "123456789ABC";

        PetResponseDTO response = petService.create(dto);

        assertNotNull(response.id);
        assertEquals(dto.name, response.name);
        assertEquals(dto.species, response.species);
        assertEquals(dto.breed, response.breed);
        assertEquals(dto.gender, response.gender);
        assertEquals(dto.size, response.size);
        assertEquals(dto.weight, response.weight);
        assertEquals(dto.bio, response.bio);
        assertTrue(response.active);
        assertNotNull(response.createdAt);
        assertNotNull(response.updatedAt);
        assertEquals(ownerId, response.ownerId);
    }

    @Test
    @DisplayName("Shoul create pet with minimal fields")
    void shouldCreatePetWithMinimalFields() {
        PetCreateDTO dto = new PetCreateDTO();
        dto.name = "Pedro o gato";
        dto.species = PetSpecies.CAT;
        dto.gender = PetGender.MALE;
        dto.ownerId = ownerId;

        PetResponseDTO response = petService.create(dto);
        assertNotNull(response.id);
        assertEquals(dto.name, response.name);
        assertEquals(dto.species, response.species);
        assertEquals(dto.gender, response.gender);
        assertEquals(ownerId, response.ownerId);
    }

    @Test
    @DisplayName("Should throw exception when owner does not exist")
    void shouldThrowExceptionWhenOwnerDoesNotExist() {
        PetCreateDTO dto = new PetCreateDTO();
        dto.name = "Pedro o gato";
        dto.species = PetSpecies.CAT;
        dto.gender = PetGender.MALE;
        dto.ownerId = 999L;

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.create(dto);
        });

        assertEquals("Owner not found with id: " + dto.ownerId, exception.getMessage());
    }
}
