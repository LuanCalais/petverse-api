package com.petverse.resource;

import com.petverse.domain.entity.Pet;
import com.petverse.domain.entity.Post;
import com.petverse.domain.entity.User;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

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
}
