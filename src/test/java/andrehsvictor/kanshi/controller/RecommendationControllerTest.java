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
                .body("content[0].rating", equalTo(recommendation.getRating().floatValue()))
                .body("content[0].anime.id", equalTo(recommendation.getAnime().getId().intValue()))
                .body("content[0].anime.title", equalTo(recommendation.getAnime().getTitle()))
                .body("content[0].anime.description", equalTo(recommendation.getAnime().getDescription()))
                .body("content[0].anime.episodes", equalTo(recommendation.getAnime().getEpisodes()))
                .body("content[0].user.username", equalTo(recommendation.getUser().getUsername()))
                .body("content[0].user.id", equalTo(recommendation.getUser().getId().intValue()))
                .body("content[0].id", equalTo(recommendation.getId().intValue()));
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
                .body("rating", equalTo(recommendation.getRating().floatValue()))
                .body("anime.id", equalTo(recommendation.getAnime().getId().intValue()))
                .body("anime.title", equalTo(recommendation.getAnime().getTitle()))
                .body("anime.description", equalTo(recommendation.getAnime().getDescription()))
                .body("anime.episodes", equalTo(recommendation.getAnime().getEpisodes()))
                .body("user.username", equalTo(recommendation.getUser().getUsername()))
                .body("user.id", equalTo(recommendation.getUser().getId().intValue()))
                .body("id", equalTo(recommendation.getId().intValue()));
    }

    @Test
    void findAllByAnimeId_shouldReturnListOfRecommendations() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/animes/" + anime.getId() + "/recommendations/all")
                .then()
                .statusCode(200)
                .body("content.size()", equalTo(1))
                .body("content[0].comment", equalTo(recommendation.getComment()))
                .body("content[0].rating", equalTo(recommendation.getRating().floatValue()))
                .body("content[0].anime.id", equalTo(recommendation.getAnime().getId().intValue()))
                .body("content[0].anime.title", equalTo(recommendation.getAnime().getTitle()))
                .body("content[0].anime.description", equalTo(recommendation.getAnime().getDescription()))
                .body("content[0].anime.episodes", equalTo(recommendation.getAnime().getEpisodes()))
                .body("content[0].user.username", equalTo(recommendation.getUser().getUsername()))
                .body("content[0].user.id", equalTo(recommendation.getUser().getId().intValue()))
                .body("content[0].id", equalTo(recommendation.getId().intValue()));
    }

    @Test
    void recommend_shouldReturnSavedRecommendation() {
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
                .body("rating", equalTo(saveRecommendationDTO.getRating().floatValue()))
                .body("anime.id", equalTo(anime.getId().intValue()))
                .body("user.username", equalTo(user.getUsername()))
                .body("user.id", equalTo(user.getId().intValue()));

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/recommendations")
                .then()
                .statusCode(200)
                .body("content.size()", equalTo(2))
                .body("content[1].comment", equalTo(saveRecommendationDTO.getComment()))
                .body("content[1].rating", equalTo(saveRecommendationDTO.getRating().floatValue()))
                .body("content[1].anime.id", equalTo(anime.getId().intValue()))
                .body("content[1].user.username", equalTo(user.getUsername()))
                .body("content[1].user.id", equalTo(user.getId().intValue()));
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
    void recommend_whenUserAlreadyRecommended_shouldReturnBadRequest() {
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
