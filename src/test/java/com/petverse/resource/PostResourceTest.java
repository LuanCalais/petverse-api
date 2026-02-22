package com.petverse.resource;

import com.petverse.domain.dto.PetCreateDTO;
import com.petverse.domain.dto.PetResponseDTO;
import com.petverse.domain.entity.Pet;
import com.petverse.domain.entity.Post;
import com.petverse.domain.entity.User;
import com.petverse.domain.enums.PetGender;
import com.petverse.domain.enums.PetSpecies;
import com.petverse.domain.service.PetService;
import com.petverse.domain.service.PostService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@DisplayName("PostResource Integration Tests")
public class PostResourceTest {

    @Inject
    PostService postService;

    @Inject
    PetService petService;

    private User ownerTest;
    private Long ownerId;
    private List<PetResponseDTO> petsTest;

    @BeforeEach
    @Transactional
    void setUp() {
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
    @DisplayName("POST /api/posts should create pet and return 201")
    void shouldCreatePostAndReturn201() {

        List<Long> petIds = petsTest.stream().map(p -> p.id).toList();
        String petIdsString = petIds.toString();

        String requestBody = String.format("""
                {
                    "content": "Bom dia, amigos",
                    "petIds": %s,
                    "ownerId": %d
                }
                """, petIdsString, ownerId);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/posts")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("content", equalTo("Bom dia, amigos"));
    }

    @Test
    @DisplayName("POST /api/posts should return 400 when content is blank")
    void shouldReturn400WhenContentIsBlank() {
        List<Long> petIds = petsTest.stream().map(p -> p.id).toList();
        String petIdsString = petIds.toString();

        String requestBody = String.format("""
                {
                    "content": "",
                    "petIds": %s,
                    "ownerId": %d
                }
                """, petIdsString, ownerId);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/posts")
                .then()
                .statusCode(400);

    }

    @Test
    @DisplayName("GET /api/posts should return post by id")
    void shouldReturnPostById() {
        Long postId = criarPostAuxiliar();

        given()
                .when()
                .get("/api/posts/{id}", postId)
                .then()
                .statusCode(200)
                .body("id", equalTo(postId.intValue()))
                .body("content", equalTo("Bom dia, amigos"));

    }

    @Test
    @DisplayName("GET /api/posts should return 404 when owner not found")
    void shouldReturn404WhenOwnerNotFound() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/posts/999")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("GET /api/pets should return list of all posts")
    void shouldReturnListOfAllPosts() {
        criarPostAuxiliar();

        given()
                .when()
                .get("api/posts")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @DisplayName("DELETE /api/pets/{id} should return 404 when post not found")
    void shouldReturn404WhenDeletingNonExistentPost() {
        given()
                .when()
                .delete("/api/posts/9999")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("DELETE /api/pets/{id} should delete post and return 204")
    void shouldDeletePostAndReturn204() {
        Long postId = criarPostAuxiliar();
        given()
                .when()
                .delete("/api/posts/{id}", postId)
                .then()
                .statusCode(204);
    }

    private Long criarPostAuxiliar() {
        List<Long> petIds = petsTest.stream().map(p -> p.id).toList();
        String petIdsString = petIds.toString();

        String requestBody = String.format("""
                {
                    "content": "Bom dia, amigos",
                    "petIds": %s,
                    "ownerId": %d
                }
                """, petIdsString, ownerId);

        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/posts")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");
    }
}
