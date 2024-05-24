package andrehsvictor.kanshi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import andrehsvictor.kanshi.entity.Anime;
import andrehsvictor.kanshi.entity.Genre;
import andrehsvictor.kanshi.util.AnimeFactory;
import jakarta.persistence.EntityManager;

@DataJpaTest
class AnimeRepositoryTest {
    
    @Autowired
    EntityManager entityManager;

    @Autowired
    AnimeRepository animeRepository;

    @Autowired
    GenreRepository genreRepository;

    @BeforeEach
    void setUp() {
        entityManager.clear();
    }

    @Test
    void findAllByTitleContainingIgnoreCase_shouldReturnAllAnimes() {
        Anime anime = AnimeFactory.create();
        entityManager.persist(anime);

        List<Anime> animes = animeRepository.findAllByTitleContainingIgnoreCase(anime.getTitle(), Pageable.unpaged()).getContent();

        assertThat(animes.size()).isEqualTo(1);
        assertThat(animes.get(0).getTitle()).isEqualTo(anime.getTitle());
    }

    @Test
    void findAllByTitleContainingIgnoreCase_shouldReturnEmptyList() {
        List<Anime> animes = animeRepository.findAllByTitleContainingIgnoreCase("title", Pageable.unpaged()).getContent();

        assertThat(animes.size()).isEqualTo(0);
    }

    @Test
    void findAllByGenre_shouldReturnAllAnimes() {
        Anime anime = AnimeFactory.create();
        Genre genre = new Genre("Action");
        genre = genreRepository.save(genre);
        anime.getGenres().add(genre);
        entityManager.persist(anime);

        List<Anime> animes = animeRepository.findAllByGenre(genre.getName(), Pageable.unpaged()).getContent();

        assertThat(animes.size()).isEqualTo(1);
        assertThat(animes.get(0).getTitle()).isEqualTo(anime.getTitle());
    }

}
