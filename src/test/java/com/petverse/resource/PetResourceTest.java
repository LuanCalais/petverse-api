package com.petverse.resource;

import com.petverse.domain.entity.Pet;
import com.petverse.domain.entity.User;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@DisplayName("PetResource Integration Tests")
public class PetResourceTest {
    private Long userId;

    @BeforeEach
    @Transactional
    void setUp() {
        Pet.deleteAll();
        User.deleteAll();

        User user = new User();
        user.name = "Fulano de Tal";
        user.email = "fulanoDeTal@mail.com";
        user.password = "senha12345";
        user.bio = "Amante de pets";
        user.persist();
        userId = user.id;
    }

    @Test
    @DisplayName("POST /api/pets should create pet and return 201")
    void shouldCreatePetAndReturn201() {
        String requestBody = """
                {
                    "name": "Rex",
                    "species": "DOG",
                    "gender": "MALE",
                    "size": "LARGE",
                    "weight": 32.5,
                    "bio": "Cachorro muito fofo",
                    "ownerId": """ + userId + """
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/pets")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Rex"))
                .body("species", equalTo("DOG"))
                .body("gender", equalTo("MALE"))
                .body("size", equalTo("LARGE"))
                .body("weight", equalTo(32.5f))
                .body("bio", equalTo("Cachorro muito fofo"))
                .body("active", equalTo(true))
                .body("ownerId", equalTo(userId.intValue()));
    }

    @Test
    @DisplayName("POST /api/pets should create pet with minimal fields")
    void shouldCreatePetWithMinimalFields() {
        String requestBody = """
                {
                    "name": "Miau",
                    "species": "CAT",
                    "gender": "FEMALE",
                    "ownerId": """ + userId + """
                }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/pets")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Miau"))
                .body("species", equalTo("CAT"))
                .body("gender", equalTo("FEMALE"))
                .body("active", equalTo(true));
    }

    @Test
    @DisplayName("POST /api/pets should return 400 when name is blank")
    void shouldReturn400WhenNameIsBlank() {
        String requestBody = """
                {
                    "name": "",
                    "species": "DOG",
                    "gender": "MALE",
                    "ownerId": """ + userId + """
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/pets")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("GET /api/pets/{id} should return 404 when pet not found")
    void shouldReturn404WhenPetNotFound() {
        given()
                .when()
                .get("/api/pets/9999")
                .then()
                .statusCode(404); // Not Found
    }

    @Test
    @DisplayName("GET /api/pets should return list of all pets")
    void shouldReturnListOfAllPets() {
        criarPetAuxiliar("Rex", "DOG");
        criarPetAuxiliar("Miau", "CAT");
        criarPetAuxiliar("Tweety", "BIRD");

        given()
                .when()
                .get("/api/pets")
                .then()
                .statusCode(200)
                .body("size()", equalTo(3))
                .body("name", hasItems("Rex", "Miau", "Tweety"));
    }

    @Test
    @DisplayName("PUT /api/pets/{id} should return 404 when pet not found")
    void shouldReturn404WhenUpdatingNonExistentPet() {
        String updateBody = """
                {
                    "name": "Novo nome"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(updateBody)
                .when()
                .put("/api/pets/9999")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("DELETE /api/pets/{id} should delete pet and return 204")
    void shouldDeletePetAndReturn204() {
        Long petId = criarPetAuxiliar("Rex", "DOG");

        given()
                .when()
                .delete("/api/pets/" + petId)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/api/pets/" + petId)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("DELETE /api/pets/{id} should return 404 when pet not found")
    void shouldReturn404WhenDeletingNonExistentPet() {
        given()
                .when()
                .delete("/api/pets/9999")
                .then()
                .statusCode(404);
    }


    private Long criarPetAuxiliar(String name, String species) {
        String requestBody = """
                {
                    "name": "%s",
                    "species": "%s",
                    "gender": "MALE",
                    "ownerId": %d
                }
                """.formatted(name, species, userId);

        Integer id = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/pets")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        return id.longValue();
    }

}
