package miniteam.moviesearch.service;


import lombok.RequiredArgsConstructor;
import miniteam.moviesearch.entity.Bookmark;
import miniteam.moviesearch.entity.User;
import miniteam.moviesearch.repository.BookmarkRepository;
import miniteam.moviesearch.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    // 로그인한 사용자 가져오기
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username) // 현재 로그인한 사용자 이름(username)을 JWT에서 꺼냄
                .orElseThrow(() -> new RuntimeException("사용자 없음")); // DB에서 해당 사용자 엔티티(User) 조회해서 리턴
    }

    // 북마크 토글
    public void addBookmark(String movieId, String title, String posterUrl) {
        User user = getCurrentUser(); // 햔제 사용자 기준으로 북마크 추가/삭제 판단

        boolean exists = bookmarkRepository.findByUserAndMovieId(user, movieId).isPresent();
        if (!exists){
            Bookmark b = new Bookmark();
            b.setUser(user);
            b.setMovieId(movieId);
            b.setTitle(title);
            b.setPosterUrl(posterUrl);
            bookmarkRepository.save(b);
        } // 하트 누르면 "토글" 느낌으로 동작하게 됨
    }

    public void removeBookmark(String movieId) {
        User user = getCurrentUser();

        bookmarkRepository.findByUserAndMovieId(user, movieId)
                .ifPresent(bookmarkRepository::delete);
    }

    public void toggleBookmark(String movieId, String title, String posterUrl) {
        User user = getCurrentUser();

        bookmarkRepository.findByUserAndMovieId(user, movieId)
                .ifPresentOrElse(
                        bookmarkRepository::delete,
                        () -> addBookmark(movieId, title, posterUrl)
                );
    }

    // 내 북마크 목록
    public List<Bookmark> getMyBookmarks() { // 현재 로그인한 사용자의 북마크만 모두 조회
        User user = getCurrentUser();
        return bookmarkRepository.findByUser(user);
    }
}
