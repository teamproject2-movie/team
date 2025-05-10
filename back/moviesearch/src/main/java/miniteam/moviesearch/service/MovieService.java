package miniteam.moviesearch.service;

import lombok.RequiredArgsConstructor;
import miniteam.moviesearch.dto.MovieDetailDto;
import miniteam.moviesearch.dto.MovieDto;
import miniteam.moviesearch.tmdb.GenreResponse;
import miniteam.moviesearch.tmdb.TmdbDetailResponse;
import miniteam.moviesearch.tmdb.TmdbSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;


// 실제로 OMDb API를 호출해서 영화 데이터를 가져오고, 내부 DTO로 변환하는 핵심 로직을 담당함
// RestTemplate으로 HTTP 요청을 보내고, 응답 JSON을 OmdbSearchResponse로 자동 매핑함
// 결과 리스트를 MovieDto 리스트로 변환해서 컨트롤러에 전달
// genres는 지금 비워놓고, 다음 단계에서 상세조회할 때 장르 정보 채울 예정
@Service        // 이 클래스는 서비스 계층에 해당하는 컴포넌트임을 명시(Spring이 자동 관리)
@RequiredArgsConstructor    // final 필드 자동 생성자 주입
public class MovieService {

    @Value("${tmdb.api.key}")
    private String apiKey;      // aplication.properties에서 설정 TmDb API 키를 주입 받음


    private final RestTemplate restTemplate;    // Spring이 제공하는 HTTP 요청 도구
    // 나중에 설정한 RestTemplateConfig를 통해 빈(Bean)으로 등록해서 주입됨

    private Map<Integer, String> genreMap = new HashMap<>();    // TMDb에서 받은 장르 리스트를 캐싱해두는 맵
    // 28 -> "Action" 이런 식으로 저장됨

    public List<MovieDto> searchMovies(String query, String language) {  // 검색어(query)를 받아서 TMDb API를 호출하고 결과를 DTO로 가공해주는 메서드
        loadGenreMapIfNeeded(); // 장르 정보 먼저 불러오기

        List<MovieDto> allResults = new ArrayList<>();
        for (int page = 1; page <= 2; page++) {
            // 영화 검색
            String url = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey +
                    "&query=" + query +
                    "&language=" + language +
                    "&page=" + page; // TMDb의 검색 API URL 생성

            // 쿼리와 API 키를 붙여 완성된 요청 URL 만들기
            TmdbSearchResponse response = restTemplate.getForObject(url, TmdbSearchResponse.class); //해당 URL로 GET요청을 보내고, 응답을 TmdbSearchResponse 객체로 자동 피싱


            if (response != null && response.getResults() != null) {
                List<MovieDto> movieDtos = response.getResults().stream().map(item -> { // results 리스트를 반복하며 각 영화 객체를 가공
                    MovieDto dto = new MovieDto();
                    dto.setId(item.getId());
                    dto.setTitle(item.getTitle());
                    dto.setReleaseDate(item.getReleaseDate());
                    dto.setPosterUrl("https://image.tmdb.org/t/p/w500" + item.getPosterPath());
                    dto.setOverview(item.getOverview());

                    List<String> genreNames = item.getGenreIds().stream() // item.getGenreIds()는 TMDb가 응답한 장르숫자 리스트 (예[28, 12]) .stream()은 리스트를 하나씩 반복해서 처리할 준비를 함( Java 스트림 문법)
                            .map(genreMap::get) // 장르 ID를 genreMap에서 꺼내서 이름으로 바꿈 예) 28 -> "Action" 즉, TMDb가 준 ID를 우리가 미리 받아둔 장르 맵에서 찾아서 바꾸는 중
                            .filter(Objects::nonNull) // 혹시라도 ID에 해당하는 장르 이름이 없을 경우(null)를 제외하는 단계
                            .collect(Collectors.toList());  // 위에서 변환된 장르 이름들을 다시 리스트형태로 모음

                    if (genreNames.isEmpty()) { // 만약 TMDb가 genre)ids를 빈 리스트로 줬다면 genreNames도 비어있을 것
                        genreNames.add("장르 없음");    // 그럴 때 빈칸 대신 "장르 없음"을 문자열에 입력
                    }
                    dto.setGenres(genreNames);  // MovieDto 객체에 genres 리스트를 넣어주는 단계
                    // 이후 이 DTO는 Controller를 통해 프론트엔드나 Swagger로 응답함

                    return dto;
                }).toList();

                allResults.addAll(movieDtos);
            }
        }

        return allResults;
    }// 각영화에 대해 MovieDto 객체를 만들고 필요한 값 설정
    // TMDb 검색 API를 호출하고, 응답을 MovieDto 리스트로 변환
    // 포스터 URL 앞에 이미지 경로 prefix를 붙여서 완성
    // 장르는 상세 조회 시 넣음


    private void loadGenreMapIfNeeded() {   // 이 메서드는 앱이 처음 실행되거나 검색 요청을 할 때 한 번만 장르 목록을 불러오도록 함
        if (!genreMap.isEmpty()) return;    // 이미 장르맵이 있으면 다시 불러올 필요 없으니까 빠져나감

        String url = "https://api.themoviedb.org/3/genre/movie/list?api_key=" + apiKey + "&language=ko-KR";; // TMDb 장르 목록을 요청하는 URL
        GenreResponse response = restTemplate.getForObject(url, GenreResponse.class);   // API 요청 -> 응답을 GenreResponse 객체로 자동 변환

        if (response != null && response.getGenres() != null) {
            genreMap = response.getGenres().stream()
                    .collect(Collectors.toMap(
                            GenreResponse.Genre::getId,
                            GenreResponse.Genre::getName
                    )); // 응답 리스트를 순회하면서 -> Map(Integer, String>으로 변환
                        // .getId()가 key, .getName()이 value
        }
    }

    public MovieDetailDto getMovieDetail(Long movieId) {    // 영화 상세 조회
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey + "&language=ko-KR";

        TmdbDetailResponse response = restTemplate.getForObject(url, TmdbDetailResponse.class);

        if (response == null) {
            throw new RuntimeException("영화 상세 정보를 가져오지 못했습니다.");
        }

        MovieDetailDto dto = new MovieDetailDto();
        dto.setId(response.getId());
        dto.setTitle(response.getTitle());
        dto.setReleaseDate(response.getReleaseDate());
        dto.setOverview(response.getOverview());
        dto.setPosterUrl("https://image.tmdb.org/t/p/w500" + response.getPosterPath());

        List<String> genreNames = response.getGenres().stream()
                .map(TmdbDetailResponse.Genre::getName)
                .collect(Collectors.toList());
        dto.setGenres(genreNames);

        return dto;
    }   // TMDb 상세정보 API 호출
        // 줄거리, 장르, 포스터, 개봉일 등 가공해서 MovieDetailDto로 변환

    public String getYoutubeTrailerUrl(Long movieId) {
        String url = "https://api,themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + apiKey + "&language=ko-KR";

        Map<String,Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, String>> results = (List<Map<String, String>>) response.get("results");

        for (Map<String, String> video : results) {
            if ("Trailer".equalsIgnoreCase(video.get("type")) && "YouTube".equalsIgnoreCase(video.get("site"))) {
                return "https://www.youtube.com/embed/" + video.get("key") + "?autoplay=1?mute=1";
            }
        }
        return null;
    }
}

