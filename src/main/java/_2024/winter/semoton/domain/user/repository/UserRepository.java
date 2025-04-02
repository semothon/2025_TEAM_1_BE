package _2024.winter.semoton.domain.user.repository;

import _2024.winter.semoton.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByAllClearedTrue();

    @Query("""
    SELECT MAX(u.totalScore)
    FROM User u
    """)
    Integer findMaxTotalScore();

}
