package miniteam.moviesearch.repository;

import miniteam.moviesearch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}   // Spring Data JPA의 기본 기능 + username으로 사용자 찾는 메서드 추가
