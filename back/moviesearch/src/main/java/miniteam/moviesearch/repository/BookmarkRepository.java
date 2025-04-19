package miniteam.moviesearch.repository;

import miniteam.moviesearch.entity.Bookmark;
import miniteam.moviesearch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByUser(User user); // 로그인한 사용자가 가진 북마크 목록 조회

    Optional<Bookmark> findByUserAndMovieId(User user, String movieId); // 특정 영화가 이미 북마크돼 있는지 확인
    // 있으면 삭제, 없으면 추가 (토글 용도)
}
