package andrehsvictor.kanshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import andrehsvictor.kanshi.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
