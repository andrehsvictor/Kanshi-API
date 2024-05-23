package andrehsvictor.kanshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import andrehsvictor.kanshi.entity.Recommendation;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    
}
