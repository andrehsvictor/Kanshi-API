package andrehsvictor.kanshi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import andrehsvictor.kanshi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(nativeQuery = true, value = """
            SELECT *
            FROM users
            WHERE email = :email
            """)
    Optional<User> findByEmail(String email);
}
