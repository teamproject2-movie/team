package miniteam.moviesearch.controller;

import lombok.RequiredArgsConstructor;
import miniteam.moviesearch.dto.MovieDetailDto;
import miniteam.moviesearch.dto.MovieDto;
import miniteam.moviesearch.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController             // 이 클래스는 REST API를 처리하는 컨트롤러 라는 뜻 (자동으로 @ResponseBody 포함)
@RequestMapping("api/movies") // 이 클래스 안의 모든 API URL앞에 /api/movies 가 붙게함
@RequiredArgsConstructor // final 필드(movieService)에 대해 생성자를 자동으로 만들어줌(Lombok)
public class MovieController {  // 실제 컨트롤러 클래스 선언

    private final MovieService movieService;    // MovieService를 의존성 주입 받음
// final 이 붙어있어서 생성자 방식으로만 주입 가능(위의 @RequiredArgsConstructor와 연동됨)

    @GetMapping("/Search")  // /api/movies/search로 GET 요청이 오면 이 메서드 실행
    public ResponseEntity<?> searchMoviessearchMoives(@RequestParam String query) { // @RequestParam String query: URL 쿼리 스트링 ?query=batman 형식의 query값을 받아옴
        //ResponseEntity<?>: 결과를 감싸서 HTTP 상태 코드와 함께 반환할 수 있게 해줌

        List<MovieDto> result = movieService.searchMovies(query);

        if (result.isEmpty()) {
            return ResponseEntity.status(404).body("검색 결과가 없습니다.");
        }
        return ResponseEntity.ok(result); // movieService.searchMovies(query): 서비스 클래스의 실제 비즈니스 로직 호출
        //ResponseEntity.ok(...): HTTP 200 응답으로 클라이언트에 결과 반환
    }

    @GetMapping("/{id}")    // /api/movies/{id} 요청을 처리 (영화 상세 조회)
    // {id}는 영화 ID, TMDb에 요청해 상세 정보 받아옴
    public ResponseEntity<?> getMovieDetail(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(movieService.getMovieDetail(id));
        } catch (Exception e) {
            return ResponseEntity.status(404).body("해당 영화가 존재하지 않습니다");
        }
    }
}
