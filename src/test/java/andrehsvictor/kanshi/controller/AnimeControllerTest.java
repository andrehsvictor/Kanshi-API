package andrehsvictor.kanshi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import andrehsvictor.kanshi.dto.AuthenticateDTO;
import andrehsvictor.kanshi.entity.Anime;
import andrehsvictor.kanshi.entity.User;
import andrehsvictor.kanshi.repository.AnimeRepository;
import andrehsvictor.kanshi.repository.RecommendationRepository;
import andrehsvictor.kanshi.repository.UserRepository;
import andrehsvictor.kanshi.util.AbstractControllerTest;
import andrehsvictor.kanshi.util.AnimeFactory;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

class AnimeControllerTest extends AbstractControllerTest {
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

    @BeforeEach
    void setUp() {
        recommendationRepository.deleteAll();
        animeRepository.deleteAll();
        userRepository.deleteAll();

        user = new User("user1", passwordEncoder.encode("P4ssword"), "user1@kanshi.io");
        userRepository.save(user);

        anime = AnimeFactory.create();
        animeRepository.save(anime);

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
    void findAll_shouldReturnPageOfAnimes() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/animes")
                .then()
                .statusCode(200)
                .body("content.size()", is(1))
                .body("content[0].title", is(anime.getTitle()))
                .body("content[0].description", is(anime.getDescription()))
                .body("content[0].episodes", is(anime.getEpisodes()))
                .body("content[0].id", is(anime.getId().intValue()))
                .body("totalElements", is(1))
                .body("totalPages", is(1))
                .body("size", is(20))
                .body("number", is(0));
    }

    @Test
    void findById_shouldReturnAnime() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/animes/" + anime.getId())
                .then()
                .statusCode(200)
                .body("title", is(anime.getTitle()))
                .body("description", is(anime.getDescription()))
                .body("episodes", is(anime.getEpisodes()))
                .body("likedBy.size()", is(0))
                .body("genres.size()", is(0))
                .body("id", is(anime.getId().intValue()));
    }

    @Test
    void findById_whenAnimeDoesNotExist_shouldReturn404() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/animes/999")
                .then()
                .statusCode(404);
    }

    @Test
    void like_shouldLikeAnime() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .patch("/animes/" + anime.getId() + "/like")
                .then()
                .statusCode(200);

        Anime updatedAnime = animeRepository.findById(anime.getId()).get();
        assertThat(updatedAnime.getLikedBy().size(), is(1));
        assertThat(updatedAnime.getLikedBy().iterator().next().getId(), is(user.getId()));

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/animes/" + anime.getId())
                .then()
                .statusCode(200)
                .body("likedBy.size()", is(1))
                .body("likedBy[0].id", is(user.getId().intValue()))
                .body("likedBy[0].username", is(user.getUsername()));
    }

    @Test
    void like_shouldUnlikeAnime() {
        anime.getLikedBy().add(user);
        animeRepository.save(anime);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .patch("/animes/" + anime.getId() + "/like")
                .then()
                .statusCode(200);

        Anime updatedAnime = animeRepository.findById(anime.getId()).get();
        assertThat(updatedAnime.getLikedBy().size(), is(0));

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/animes/liked")
                .then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void like_whenAnimeDoesNotExist_shouldReturn404() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .patch("/animes/999/like")
                .then()
                .statusCode(404);
    }

    @Test
    void findLikedAnimes_shouldReturnSetOfAnimes() {
        anime.getLikedBy().add(user);
        animeRepository.save(anime);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/animes/liked")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].id", is(anime.getId().intValue()))
                .body("[0].title", is(anime.getTitle()))
                .body("[0].description", is(anime.getDescription()))
                .body("[0].episodes", is(anime.getEpisodes()));
    }

    @Test
    void findLikedAnimes_whenUserHasNoLikedAnimes_shouldReturnEmptySet() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/animes/liked")
                .then()
                .statusCode(200)
                .body("size()", is(0));
    }

}
