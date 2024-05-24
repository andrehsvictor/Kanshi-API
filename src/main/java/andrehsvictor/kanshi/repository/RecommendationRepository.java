package andrehsvictor.kanshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import andrehsvictor.kanshi.entity.Recommendation;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    @Query(nativeQuery = true, value = """
            SELECT recommendations.*
            FROM recommendations
            JOIN animes ON recommendations.anime_id = :animeId
            """)
    Page<Recommendation> findAllByAnimeId(Long animeId, Pageable pageable);
}
