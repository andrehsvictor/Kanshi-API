package andrehsvictor.kanshi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import andrehsvictor.kanshi.dto.AuthenticateDTO;
import andrehsvictor.kanshi.dto.SaveOrUpdateRecommendationDTO;
import andrehsvictor.kanshi.entity.Anime;
import andrehsvictor.kanshi.entity.Recommendation;
import andrehsvictor.kanshi.entity.User;
import andrehsvictor.kanshi.repository.AnimeRepository;
import andrehsvictor.kanshi.repository.RecommendationRepository;
import andrehsvictor.kanshi.repository.UserRepository;
import andrehsvictor.kanshi.util.AbstractControllerTest;
import andrehsvictor.kanshi.util.AnimeFactory;
import andrehsvictor.kanshi.util.RecommendationFactory;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.equalTo;

class RecommendationControllerTest extends AbstractControllerTest {

    @Autowired
    RecommendationRepository recommendationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AnimeRepository animeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    User user;

    String token;

    Anime anime;

    Recommendation recommendation;

    @BeforeEach
    void setUp() {
        recommendationRepository.deleteAll();
        animeRepository.deleteAll();
        userRepository.deleteAll();

        user = new User("user1", passwordEncoder.encode("P4ssword"), "user1@kanshi.io");
        userRepository.save(user);

        anime = AnimeFactory.create();
        animeRepository.save(anime);

        recommendation = RecommendationFactory.create();
        recommendation.setUser(user);
        recommendation.setAnime(anime);
        recommendationRepository.save(recommendation);

        token = given()
                .contentType("application/json")
                .body(new AuthenticateDTO("user1@kanshi.io", "P4ssword"))
                .when()
                .post("/authenticate")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @Test
    void findAll_shouldReturnListOfRecommendations() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/recommendations")
                .then()
                .statusCode(200)
                .body("content.size()", equalTo(1))
                .body("content[0].comment", equalTo(recommendation.getComment()))
                .body("content[0].rating", equalTo(recommendation.getRating().floatValue()));
    }

    @Test
    void findById_shouldReturnRecommendation() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/recommendations/" + recommendation.getId())
                .then()
                .statusCode(200)
                .body("comment", equalTo(recommendation.getComment()))
                .body("rating", equalTo(recommendation.getRating().floatValue()));
    }

    @Test
    void save_shouldReturnSavedRecommendation() {
        SaveOrUpdateRecommendationDTO saveRecommendationDTO = new SaveOrUpdateRecommendationDTO();
        saveRecommendationDTO.setComment("Great anime");
        saveRecommendationDTO.setRating(9.0);
        Anime anime = animeRepository.save(AnimeFactory.create());

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(saveRecommendationDTO)
                .when()
                .post("/animes/" + anime.getId() + "/recommendations")
                .then()
                .statusCode(200)
                .body("comment", equalTo(saveRecommendationDTO.getComment()))
                .body("rating", equalTo(saveRecommendationDTO.getRating().floatValue()));

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/recommendations")
                .then()
                .statusCode(200)
                .body("content.size()", equalTo(2));
    }

    @Test
    void delete_shouldDeleteRecommendation() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/recommendations/" + recommendation.getId())
                .then()
                .statusCode(204);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/recommendations")
                .then()
                .statusCode(200)
                .body("content.size()", equalTo(0));
    }

    @Test
    void update_shouldUpdateRecommendation() {
        SaveOrUpdateRecommendationDTO updateRecommendationDTO = new SaveOrUpdateRecommendationDTO();
        updateRecommendationDTO.setComment("Great anime");
        updateRecommendationDTO.setRating(9.0);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(updateRecommendationDTO)
                .when()
                .put("/recommendations/" + recommendation.getId())
                .then()
                .statusCode(200)
                .body("comment", equalTo(updateRecommendationDTO.getComment()))
                .body("rating", equalTo(updateRecommendationDTO.getRating().floatValue()));
    }

    @Test
    void save_whenUserAlreadyRecommended_shouldReturnBadRequest() {
        SaveOrUpdateRecommendationDTO saveRecommendationDTO = new SaveOrUpdateRecommendationDTO();
        saveRecommendationDTO.setComment("Great anime");
        saveRecommendationDTO.setRating(9.0);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(saveRecommendationDTO)
                .when()
                .post("/animes/" + anime.getId() + "/recommendations")
                .then()
                .statusCode(400);
    }

    @Test
    void delete_whenUserIsNotOwner_shouldReturnForbidden() {
        User anotherUser = new User("user2", passwordEncoder.encode("P4ssword"), "user2@kanshi.io");
        userRepository.save(anotherUser);

        String anotherToken = given()
                .contentType("application/json")
                .body(new AuthenticateDTO("user2@kanshi.io", "P4ssword"))
                .when()
                .post("/authenticate")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + anotherToken)
                .when()
                .delete("/recommendations/" + recommendation.getId())
                .then()
                .statusCode(403);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/recommendations")
                .then()
                .statusCode(200)
                .body("content.size()", equalTo(1));

    }

    @Test
    void update_whenUserIsNotOwner_shouldReturnForbidden() {
        User anotherUser = new User("user2", passwordEncoder.encode("P4ssword"), "user2@kanshi.io");
        userRepository.save(anotherUser);

        String anotherToken = given()
                .contentType("application/json")
                .body(new AuthenticateDTO("user2@kanshi.io", "P4ssword"))
                .when()
                .post("/authenticate")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        SaveOrUpdateRecommendationDTO updateRecommendationDTO = new SaveOrUpdateRecommendationDTO();
        updateRecommendationDTO.setComment("Great anime");
        updateRecommendationDTO.setRating(9.0);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + anotherToken)
                .body(updateRecommendationDTO)
                .when()
                .put("/recommendations/" + recommendation.getId())
                .then()
                .statusCode(403);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/recommendations")
                .then()
                .statusCode(200)
                .body("content.size()", equalTo(1))
                .body("content[0].comment", equalTo(recommendation.getComment()))
                .body("content[0].rating", equalTo(recommendation.getRating().floatValue()));

    }
}
