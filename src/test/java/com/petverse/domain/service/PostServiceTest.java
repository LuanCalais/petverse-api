package com.petverse.domain.service;

import com.petverse.domain.dto.*;
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

    @Test
    @DisplayName("Should throw exception when post does not exist")
    void shouldThrowExceptionWhenPostNotExist() {
        Long id = 999L;

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            postService.findById(id);
        });

        assertEquals("Post not found with id: " + id, exception.getMessage());
    }

    @Test
    @DisplayName("Should get post by id")
    void shouldGetPostById() {
        PostCreateDTO dto = new PostCreateDTO();
        dto.content = "Bom dia, amigos";
        dto.petIds = petsTest.stream().map(p -> p.id).toList();
        dto.ownerId = ownerId;

        PostResponseDTO response = postService.create(dto);
        postService.findById(response.id);

        assertNotNull(response.id);
        assertEquals(dto.content, response.content);
        assertEquals(dto.petIds.size(), response.pets.size());
        assertEquals(dto.ownerId, response.ownerId);
    }

    @Test
    @DisplayName("Should get posts")
    void shouldGetPosts() {
        PostCreateDTO dto1 = new PostCreateDTO();
        dto1.content = "Bom dia, amigos";
        dto1.petIds = petsTest.stream().map(p -> p.id).toList();
        dto1.ownerId = ownerId;

        PostCreateDTO dto2 = new PostCreateDTO();
        dto2.content = "Bom dia, amigos (segundo)";
        dto2.petIds = petsTest.stream().map(p -> p.id).toList();
        dto2.ownerId = ownerId;

        PostResponseDTO responseDTO1 = postService.create(dto1);
        PostResponseDTO responseDTO2 = postService.create(dto2);

        List<PostResponseDTO> responseDTOS = postService.listAll();

        assertEquals(2, responseDTOS.size());
        assertTrue(responseDTOS.stream().anyMatch(p -> p.content.equals("Bom dia, amigos")));
        assertTrue(responseDTOS.stream().anyMatch(p -> p.content.equals("Bom dia, amigos (segundo)")));
        assertTrue(responseDTOS.stream().allMatch(p -> p.ownerId.equals(ownerId)));
        assertTrue(responseDTOS.stream().allMatch(p -> p.active));
    }

    @Test
    @DisplayName("Should thrown exception when post is not found soft deleted")
    void shouldThrowExceptionWhenPosttNotFoundInSoftDeletePost() {
        PostCreateDTO dto = new PostCreateDTO();
        dto.content = "Bom dia, amigos";
        dto.petIds = petsTest.stream().map(p -> p.id).toList();
        dto.ownerId = ownerId;
        PostResponseDTO created = postService.create(dto);

        postService.delete(created.id);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            postService.findById(created.id);
        });

        assertEquals("Post not found with id: " + created.id, exception.getMessage());
    }

    @Test
    @DisplayName("Should update post successfully")
    void shouldUpdatePostSuccessfully() {
        PostCreateDTO createDTO = new PostCreateDTO();
        createDTO.content = "Bom dia, amigos";
        createDTO.petIds = petsTest.stream().map(p -> p.id).toList();
        createDTO.ownerId = ownerId;
        PostResponseDTO created = postService.create(createDTO);

        PostUpdateDTO updateDTO = new PostUpdateDTO();
        updateDTO.content = "Bom dia, amigos [EDITADO]";
        PostResponseDTO updated = postService.update(created.id, updateDTO);

        assertEquals(created.id, updated.id);
    }
}
