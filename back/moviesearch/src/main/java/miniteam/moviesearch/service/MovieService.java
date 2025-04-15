package miniteam.moviesearch.service;

import lombok.RequiredArgsConstructor;
import miniteam.moviesearch.dto.MovieDetailDto;
import miniteam.moviesearch.dto.MovieDto;
import miniteam.moviesearch.tmdb.TmdbDetailResponse;
import miniteam.moviesearch.tmdb.TmdbSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public List<MovieDto> searchMovies(String query) {  // 검색어(query)를 받아서 TMDb API를 호출하고 결과를 DTO로 가공해주는 메서드
        // 영화 검색
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + query; // TMDb의 검색 API URL 생성
        // 쿼리와 API 키를 붙여 완성된 요청 URL 만들기
        TmdbSearchResponse response = restTemplate.getForObject(url, TmdbSearchResponse.class); //해당 URL로 GET요청을 보내고, 응답을 TmdbSearchResponse 객체로 자동 피싱


        if (response == null || response.getResults() == null) {
            return Collections.emptyList();
        }   // 응답이 비어 있거나 result리스트가 없을 경우 빈 리스트 반환 (null 방지)

        return response.getResults().stream().map(item -> { // results 리스트를 반복하며 각 영화 객체를 가공
            MovieDto dto = new MovieDto();
            dto.setId(item.getId());
            dto.setTitle(item.getTitle());
            dto.setReleaseDate(item.getReleaseDate());
            dto.setPosterUrl("https://image.tmdb.org/t/p/w500" + item.getPosterPath());
            dto.setGenres(new ArrayList<>());
            return dto;
        }).collect(Collectors.toList());
    }// 각영화에 대해 MovieDto 객체를 만들고 필요한 값 설정
    // TMDb 검색 API를 호출하고, 응답을 MovieDto 리스트로 변환
    // 포스터 URL 앞에 이미지 경로 prefix를 붙여서 완성
    // 장르는 상세 조회 시 넣음

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
}

