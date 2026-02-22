package com.petverse.resource;

import com.petverse.domain.entity.Pet;
import com.petverse.domain.entity.Post;
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
@DisplayName("UserResource Integration Tests")
class UserResourceTest {

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        Post.deleteAll();
        Pet.deleteAll();
        User.deleteAll();
    }

    @Test
    @DisplayName("POST /api/users should create user and return 201")
    void shouldCreateUserAndReturn201() {
        String requestBody = """
                {
                     "name": "Fulano de tal",
                     "email": "fulanoDeTal@email.com",
                     "password": "senha12345",
                     "phone": "+5511999999999",
                     "bio": "Amante de pets"
                }
                """;

        given().contentType(ContentType.JSON).body(requestBody).when().post("/api/users").then().statusCode(201).body("id", notNullValue()).body("name", equalTo("Fulano de tal")).body("active", equalTo(true)).body("password", nullValue());
    }

    @Test
    @DisplayName("POST /api/users should return 400 when email is invalid")
    void shouldReturn400WhenEmailIsInvalid() {
        String requestBody = """ 
                {
                     "name": "Fulano de tal",
                     "email": "fulanoDeTal@email.com",
                     "password": "senha12345",
                     "phone": "+5511999999999",
                     "bio": "Amante de pets"
                }
                """;

        given().contentType(ContentType.JSON).body(requestBody).when().post("/api/users");

        given().contentType(ContentType.JSON).body(requestBody).when().post("/api/users").then().statusCode(400).body("message", equalTo("User already exists with this e-mail"));
    }

    @Test
    @DisplayName("GET /api/users/{id} should return user by ID")
    void shouldReturnUserById() {
        Long id = createUser("Fulano", "fulanoDeTal@email.com");

        given()
                .when()
                .get("/api/users/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id.intValue()))
                .body("name", equalTo("Fulano"))
                .body("active", equalTo(true))
                .body("email", equalTo("fulanoDeTal@email.com"));

    }

    @Test
    @DisplayName("GET /api/users/{id} should return 404 when user not found")
    void shouldReturn404WhenUserNotFound() {
        final long id = 999L;

        given()
                .when()
                .get("/api/users/" + 999)
                .then()
                .statusCode(404)
                .body("error", equalTo("Resource Not Found"))
                .body("message", equalTo("User not found with id: " + id));


    }

    @Test
    @DisplayName("GET /api/users/email/{email} should return user by ID")
    void shouldReturnUserByEmail() {
        String email = "fulanoDeTal@email.com";
        createUser("Fulano", email);

        given()
                .when()
                .get("/api/users/email/" + email)
                .then()
                .statusCode(200)
                .body("email", equalTo(email))
                .body("name", equalTo("Fulano"))
                .body("active", equalTo(true))
                .body("password", nullValue());
    }

    @Test
    @DisplayName("PUT /api/users/{id} should update user")
    void shouldUpdateUser() {
        Long userId = createUser("Fulano", "fulanoDeTal@email.com");

        String updateBody = """
                {
                    "name": "Fulano atualizado",
                    "bio": "Nova bio"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(updateBody)
                .when()
                .put("/api/users/" + userId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Fulano atualizado"))
                .body("bio", equalTo("Nova bio"))
                .body("email", equalTo("fulanoDeTal@email.com"));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} should delete user and return 204")
    void shouldDeleteUser() {
        Long id = createUser("Fulano", "fulanoDeTal@email.com");

        given()
                .when()
                .delete("/api/users/" + id)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/api/users/" + id)
                .then()
                .statusCode(404);
    }

    private Long createUser(String name, String email) {
        String requestBody = String.format("""
                {
                    "name": "%s",
                    "email": "%s",
                    "password": "senha12345",
                    "bio": "Amante de pets"
                }
                """, name, email);


        Integer id = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract().path("id");

        return id.longValue();
    }
}

