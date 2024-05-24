package andrehsvictor.kanshi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;

import andrehsvictor.kanshi.entity.Anime;
import andrehsvictor.kanshi.entity.Recommendation;
import andrehsvictor.kanshi.entity.User;
import andrehsvictor.kanshi.util.AnimeFactory;
import andrehsvictor.kanshi.util.RecommendationFactory;
import andrehsvictor.kanshi.util.UserFactory;

@DataJpaTest
class RecommendationRepositoryTest {

    @Autowired
    RecommendationRepository recommendationRepository;

    @Autowired
    AnimeRepository animeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.clear();
    }

    @Test
    void findAllByAnimeId_shouldReturnAllRecommendations() {
        Recommendation recommendation = RecommendationFactory.create();

        Anime anime = AnimeFactory.create();
        anime = animeRepository.save(anime);
        recommendation.setAnime(anime);

        User user = UserFactory.create();
        user = userRepository.save(user);
        recommendation.setUser(user);

        entityManager.persist(recommendation);

        List<Recommendation> recommendations = recommendationRepository
                .findAllByAnimeId(recommendation.getAnime().getId(), Pageable.unpaged()).getContent();

        assertThat(recommendations.size()).isEqualTo(1);
        assertThat(recommendations.get(0).getAnime().getId()).isEqualTo(recommendation.getAnime().getId());
    }
}
