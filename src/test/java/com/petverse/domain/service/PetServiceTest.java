package com.petverse.domain.service;

import com.petverse.domain.dto.PetCreateDTO;
import com.petverse.domain.dto.PetResponseDTO;
import com.petverse.domain.dto.pet.PetUpdateDTO;
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
    @DisplayName("Should find pet by ID")
    void shouldFindPetById() {
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

        PetResponseDTO created = petService.create(dto);
        PetResponseDTO founded = petService.findById(created.id);

        assertEquals(created.id, founded.id);
        assertEquals(created.name, founded.name);
        assertEquals(created.species, founded.species);
    }

    @Test
    @DisplayName("Should create pet with minimal fields")
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

    @Test
    @DisplayName("Should throw exception when pet does not exist")
    void shouldThrowExceptionWhenPetNotExist() {
        Long id = 999L;

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.findById(id);
        });

        assertEquals("Pet not found with id: " + id, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when pet dos not exist on update")
    void shouldThrowExceptionWhenPetNotExistOnUpdate() {
        final Long testId = 9999L;
        PetUpdateDTO dto = createUpdateDTO();

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.update(testId, dto);
        });

        assertEquals("Pet not found with id: " + testId, exception.getMessage());
    }

    @Test
    @DisplayName("Should update pet successfully")
    void shouldUpdatePetSuccessfully() {
        PetCreateDTO createDTO = createMinimalPet();
        PetResponseDTO created = petService.create(createDTO);

        PetUpdateDTO updateDTO = createUpdateDTO();

        PetResponseDTO response = petService.update(created.id, updateDTO);

        assertEquals(created.id, response.id);
        assertEquals("Nome Atualizado", response.name);
        assertEquals("Bio atualizada para teste", response.bio);
        assertNotEquals(created.name, response.name);
        assertNotEquals(created.bio, response.bio);
        assertNotEquals(created.weight, response.weight);
    }

    @Test
    @DisplayName("Should thrown exception when pet is not found in soft deleted")
    void shouldThrowExceptionWhenPetNotFoundInSoftDeletePet() {
        PetCreateDTO createDTO = createMinimalPet();
        PetResponseDTO created = petService.create(createDTO);

        petService.delete(created.id);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.findById(created.id);
        });

        assertEquals("Pet not found with id: " + created.id, exception.getMessage());
    }

    private PetCreateDTO createMinimalPet() {
        PetCreateDTO dto = new PetCreateDTO();
        dto.name = "Pedro o gato";
        dto.species = PetSpecies.CAT;
        dto.gender = PetGender.MALE;
        dto.ownerId = ownerId;
        return dto;
    }

    private PetUpdateDTO createUpdateDTO() {
        PetUpdateDTO dto = new PetUpdateDTO();
        dto.name = "Nome Atualizado";
        dto.bio = "Bio atualizada para teste";
        dto.weight = 25.0;
        return dto;
    }

}
