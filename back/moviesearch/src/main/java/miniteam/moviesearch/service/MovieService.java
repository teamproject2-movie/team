package miniteam.moviesearch.service;

import lombok.RequiredArgsConstructor;
import miniteam.moviesearch.dto.MovieDto;
import miniteam.moviesearch.dto.OmdbSearchResponse;
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
@Service
@RequiredArgsConstructor
public class MovieService {

    @Value("${omdb.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public List<MovieDto> searchMoives(String title) {
        String url = "http://www.omdbapi.com/?apikey=" + apiKey + "&s=" + title + "&type=movie";
        OmdbSearchResponse response = restTemplate.getForObject(url, OmdbSearchResponse.class);

        if (response == null || !"True".equals(response.getResponse())) {
            return Collections.emptyList();
        }

        return response.getSearch().stream().map(item -> {
            MovieDto dto = new MovieDto();
            dto.setOmdbID(item.getOmdbID());
            dto.setTitle(item.getTitle());
            dto.setYear(item.getYear());
            dto.setPoster(item.getPoster());
            dto.setGenres(new ArrayList<>());
            return dto;
        }).collect(Collectors.toList());
    }
}
