package com.petverse.resource;

import com.petverse.domain.dto.RegisterRequestDTO;
import com.petverse.domain.entity.Pet;
import com.petverse.domain.entity.Post;
import com.petverse.domain.entity.User;
import com.petverse.domain.service.AuthService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@DisplayName("AuthResource Integration Tests")
class AuthResourceTest {

    @Inject
    AuthService authService;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        Post.deleteAll();
        Pet.deleteAll();
        User.deleteAll();
    }

    @Test
    @DisplayName("POST /api/auth register user and return 201")
    void shouldRegisterUserAndReturn201() {
        String requestBody = """
                {
                     "name": "Fulano de tal",
                     "email": "fulanoDeTal@email.com",
                     "password": "senha12345",
                     "phone": "+5511999999999"
                }
                """;
        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(201)
                .body("token", notNullValue())
                .body("user.id", notNullValue())
                .body("user.name", equalTo("Fulano de tal"))
                .body("user.active", equalTo(true));

    }

    @Test
    @DisplayName("POST /api/auth/login login user and return 200")
    void shouldLoginUserAndReturn200() {
        createRegistry();

        String requestBody = """
                {
                     "email": "fulanoDeTal@email.com",
                     "password": "senha123"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("user.id", notNullValue())
                .body("user.name", equalTo("Fulano de tal"))
                .body("user.active", equalTo(true))
                .body("user.email", equalTo("fulanoDeTal@email.com".toLowerCase()));
    }

    @Test
    @DisplayName("GET /api/auth/me get user and return 200")
    void shouldGetUserAndReturn200() {
        String token = createRegistryAndReturnToken();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/auth/me")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("GET /api/auth/me without token should return 401")
    void shouldReturn401WhenNoToken() {
        given()
                .when()
                .get("/api/auth/me")
                .then()
                .statusCode(401);
    }

    private void createRegistry() {
        RegisterRequestDTO registerDTO = new RegisterRequestDTO();
        registerDTO.name = "Fulano de tal";
        registerDTO.email = "fulanoDeTal@email.com";
        registerDTO.password = "senha123";
        registerDTO.phone = "+5511999999999";
        authService.register(registerDTO);
    }

    private String createRegistryAndReturnToken() {
        RegisterRequestDTO registerDTO = new RegisterRequestDTO();
        registerDTO.name = "Fulano de tal";
        registerDTO.email = "fulanoDeTal@email.com";
        registerDTO.password = "senha123";
        registerDTO.phone = "+5511999999999";
        return authService.register(registerDTO).token;
    }
}
