package miniteam.moviesearch.controller;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import miniteam.moviesearch.entity.Bookmark;
import miniteam.moviesearch.service.BookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 북마크 관련 REST API
@RequestMapping // /api/bookmarks 로 시작하는 모든 요청 처리
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 북마크 토클(추가 and 삭제)
    @PostMapping
    public ResponseEntity<?> toggleBookmark(@RequestBody BookmarkRequest request) {
        //클라이언트가 JSON으로 영화 정보를 보내면 해당 영화에 대한 북마크를 추가 또는 삭제

        bookmarkService.toggleBookmark(request.getMovieId(), request.getTitle(), request.getPosterUrl());
        return ResponseEntity.ok("처리 완료");
    }

    // 내가 북마크한 영화들
    @GetMapping
    public ResponseEntity<List<Bookmark>> getBookmarks() {
        return ResponseEntity.ok(bookmarkService.getMyBookmarks());
    } // 현재 로그인한 사용자에 대한 북마크 목록을 조회해서 반환

    @Data
    public static class BookmarkRequest {
        private String movieId;
        private String title;
        private String posterUrl;
    } // 하트 누를 때 보내는 JSON 본문을 받는 DTO
}
