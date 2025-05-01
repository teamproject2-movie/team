package miniteam.moviesearch.controller;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import miniteam.moviesearch.dto.BookmarkRequest;
import miniteam.moviesearch.dto.MovieDetailDto;
import miniteam.moviesearch.entity.Bookmark;
import miniteam.moviesearch.entity.User;
import miniteam.moviesearch.service.BookmarkService;
import miniteam.moviesearch.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController // 북마크 관련 REST API
@RequestMapping("/api/bookmarks") // /api/bookmarks 로 시작하는 모든 요청 처리
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final MovieService movieService;

    @GetMapping("/check/{movieId}")
    public ResponseEntity<?> isBookmarked(@PathVariable String movieId) {
        boolean bookmarked = bookmarkService.isBookmarked(movieId);
        return ResponseEntity.ok(Collections.singletonMap("bookmarked", bookmarked));
    }

    // 북마크 토클(추가 and 삭제)
    @PostMapping("/likes")
    public ResponseEntity<?> addBookmark(@RequestBody BookmarkRequest request) {
        //클라이언트가 JSON으로 영화 정보를 보내면 해당 영화에 대한 북마크를 추가 또는 삭제

        bookmarkService.addBookmark(request.getMovieId());
        return ResponseEntity.ok("북마크 추가됨");
    }

    @DeleteMapping("/unlikes/{movieId}")
    public ResponseEntity<?> removeBookmark(@PathVariable String movieId) {
        bookmarkService.removeBookmark(movieId);
        return ResponseEntity.ok("북마크 삭제됨");
    }

    @PostMapping("/toggle")
    public ResponseEntity<?> toogleBookmark(@RequestBody BookmarkRequest request) {
        bookmarkService.toggleBookmark(request.getMovieId());
        return ResponseEntity.ok("북마크 토글 완료");
    }

    // 내가 북마크한 영화들
    @GetMapping("/lists")
    public ResponseEntity<List<MovieDetailDto>> getBookmarkedMovieDetails() {
        List<Bookmark> bookmarks = bookmarkService.getMyBookmarks();

        List<MovieDetailDto> details = bookmarks.stream()
                .map(b -> movieService.getMovieDetail(Long.valueOf(b.getMovieId())))
                .toList();
        return ResponseEntity.ok(details);
    } // 현재 로그인한 사용자에 대한 북마크 목록을 조회해서 반환
}
