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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@DisplayName("AuthResource Integration Tests")
class AuthResourceTest {
    @BeforeEach
    @Transactional
    void cleanDatabase() {
        Post.deleteAll();
        Pet.deleteAll();
        User.deleteAll();
    }

    @Test
    @DisplayName("POST /api/auth/register register user and return 201")
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
}
