package com.petverse.domain.service;

import com.petverse.domain.dto.PetCreateDTO;
import com.petverse.domain.dto.PetResponseDTO;
import com.petverse.domain.dto.PostCreateDTO;
import com.petverse.domain.dto.PostResponseDTO;
import com.petverse.domain.entity.Pet;
import com.petverse.domain.entity.Post;
import com.petverse.domain.entity.User;
import com.petverse.domain.enums.PetGender;
import com.petverse.domain.enums.PetSpecies;
import com.petverse.exception.ResourceNotFoundException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("PostSevice Unit Tests")
public class PostServiceTest {

    @Inject
    PostService postService;

    @Inject
    PetService petService;

    private User ownerTest;
    private Long ownerId;
    private List<PetResponseDTO> petsTest;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        Post.deleteAll();
        Pet.deleteAll();
        User.deleteAll();

        ownerTest = new User();
        ownerTest.name = "João Silva";
        ownerTest.email = "joao@test.com";
        ownerTest.password = "senha12345";
        ownerTest.bio = "Amante de pets";
        ownerTest.persist();
        ownerId = ownerTest.id;

        PetCreateDTO pet1DTO = new PetCreateDTO();
        pet1DTO.name = "Pedro o gato";
        pet1DTO.species = PetSpecies.CAT;
        pet1DTO.gender = PetGender.MALE;
        pet1DTO.ownerId = ownerId;

        PetCreateDTO pet2DTO = new PetCreateDTO();
        pet2DTO.name = "Jacó o cão fêmea";
        pet2DTO.species = PetSpecies.DOG;
        pet2DTO.gender = PetGender.MALE;
        pet2DTO.ownerId = ownerId;

        petsTest = List.of(
                petService.create(pet1DTO),
                petService.create(pet2DTO)
        );
    }

    @Test
    @DisplayName("Should create post successfully")
    void shouldCreatePostSuccessfully() {
        PostCreateDTO dto = new PostCreateDTO();
        dto.content = "Bom dia, amigos";
        dto.petIds = petsTest.stream().map(p -> p.id).toList();
        dto.ownerId = ownerId;

        PostResponseDTO response = postService.create(dto);

        assertNotNull(response.id);
        assertEquals(dto.content, response.content);
        assertEquals(dto.petIds.size(), response.pets.size());
        assertEquals(dto.ownerId, response.ownerId);
    }

    @Test
    @DisplayName("Should throw exception when owner does not exist")
    void shouldThrowExceptionWhenOwnerDoesNotExist() {
        PostCreateDTO dto = new PostCreateDTO();
        dto.content = "Bom dia, amigos";
        dto.petIds = petsTest.stream().map(p -> p.id).toList();
        dto.ownerId = 999L;

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            postService.create(dto);
        });

        assertEquals("Owner not found with id: " + dto.ownerId, exception.getMessage());
    }
}
