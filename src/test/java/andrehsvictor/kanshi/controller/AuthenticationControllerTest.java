package andrehsvictor.kanshi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import andrehsvictor.kanshi.entity.User;
import andrehsvictor.kanshi.repository.RecommendationRepository;
import andrehsvictor.kanshi.repository.UserRepository;
import andrehsvictor.kanshi.util.AbstractControllerTest;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.notNullValue;

class AuthenticationControllerTest extends AbstractControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RecommendationRepository recommendationRepository;

    User user;

    @BeforeEach
    void setUp() {
        recommendationRepository.deleteAll();
        userRepository.deleteAll();

        user = new User("user1", passwordEncoder.encode("P4ssword"), "user1@kanshi.io");
        userRepository.save(user);
    }

    @Test
    void authenticate_shouldReturnToken() {
        given()
                .body("{\"username\": \"user1\", \"password\": \"P4ssword\"}")
                .contentType("application/json")
                .when()
                .post("/authenticate")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    void authenticate_shouldReturnUnauthorized() {
        given()
                .body("{\"username\": \"user1\", \"password\": \"wrongpassword\"}")
                .contentType("application/json")
                .when()
                .post("/authenticate")
                .then()
                .statusCode(401);
    }
}
