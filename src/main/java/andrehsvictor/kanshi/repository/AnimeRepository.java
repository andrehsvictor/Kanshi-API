package andrehsvictor.kanshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import andrehsvictor.kanshi.entity.Anime;

public interface AnimeRepository extends JpaRepository<Anime, Long> {
    @Query(nativeQuery = true, value = """
            SELECT *
            FROM animes
            WHERE LOWER(animes.title) LIKE LOWER(CONCAT('%', :title, '%'))
            """)
    Page<Anime> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query(nativeQuery = true, value = """
            SELECT animes.*
            FROM animes
            JOIN has_genre ON animes.id = has_genre.anime_id
            JOIN genres ON has_genre.genre_id = genres.id
            WHERE genres.name = :genre
            """)
    Page<Anime> findAllByGenre(String genre, Pageable pageable);
}
