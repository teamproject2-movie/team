package miniteam.moviesearch.service;


import lombok.RequiredArgsConstructor;
import miniteam.moviesearch.dto.MovieDetailDto;
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
    private final MovieService movieService;

    // 로그인한 사용자 가져오기
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username) // 현재 로그인한 사용자 이름(username)을 JWT에서 꺼냄
                .orElseThrow(() -> new RuntimeException("사용자 없음")); // DB에서 해당 사용자 엔티티(User) 조회해서 리턴
    }

    public boolean isBookmarked(String movieId) {
        User user = getCurrentUser();
        return bookmarkRepository.findByUserAndMovieId(user, movieId).isPresent();
    }

    // 북마크 토글
    public void addBookmark(String movieId) {
        User user = getCurrentUser(); // 햔제 사용자 기준으로 북마크 추가/삭제 판단

        boolean exists = bookmarkRepository.findByUserAndMovieId(user, movieId).isPresent();
        if (!exists){
            Bookmark b = new Bookmark();
            b.setUser(user);
            b.setMovieId(movieId);
            bookmarkRepository.save(b);
        } // 하트 누르면 "토글" 느낌으로 동작하게 됨
    }

    public void removeBookmark(String movieId) {
        User user = getCurrentUser();

        bookmarkRepository.findByUserAndMovieId(user, movieId)
                .ifPresent(bookmarkRepository::delete);
    }

    public void toggleBookmark(String movieId) {
        User user = getCurrentUser();

        bookmarkRepository.findByUserAndMovieId(user, movieId)
                .ifPresentOrElse(
                        bookmarkRepository::delete,
                        () -> addBookmark(movieId)
                );
    }

    // 내 북마크 목록
    public List<Bookmark> getMyBookmarks() { // 현재 로그인한 사용자의 북마크만 모두 조회
        User user = getCurrentUser();
        return bookmarkRepository.findByUser(user);
    }

    // 북마크 상세 정보 리스트
    public List<MovieDetailDto> getMyBookmarksWithDetail() { // 이 메서드는 북마크된 영화들의 살세 정보(MovieDetailDto) 리스트를 반환함.
        User user = getCurrentUser();   // 현재 로그인된 사용자를 JWT에서 가져옴, 이 사용자를 기준으로 목록을 조회함
        List<Bookmark> bookmarks = bookmarkRepository.findByUser(user); // 해당 사용자가 저장한 북마크들을 전부 DB에서 조회함. 결과는 Bookmark 엔티티 리스트로 옴

        return bookmarks.stream()// 리스트를 스트림으로 변환해서 각 북마크를 순차 처리하기 시작함
                .map(bookmark -> { // 각 Bookmark 객체를 MovieDetailDto 객체로 변환할 예정
                    try{
                        return movieService.getMovieDetail(Long.valueOf(bookmark.getMovieId())); // TMDB API에서 해당 movieId에 대한 상세 정보를 가져옴, movieId는 String타입이랑 Long.valueOf()로 변환 필요, 결과는 MovieDetailDto로 반환됨
                    } catch (Exception e) {
                        System.err.println("TMDB API 오류: " + bookmark.getMovieId());
                        return null; // 만약 API호출 실패 시 에러 출력 및 null 반환
                    }
                })
                .filter(detail -> detail != null) // API 호출 실패한 경우 null 필터링해서 리스트에 포함되지 않도록 함
                .toList();// 최종적으로 List
    }
}
