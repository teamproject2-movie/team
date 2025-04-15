package miniteam.moviesearch.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TmdbDetailResponse {
    private Long id;
    private String title;

    @JsonProperty("release_date")
    private String releaseDate;

    private String overview;

    @JsonProperty("poster_path")
    private String posterPath;

    private List<Genre> genres;

    @Data
    public static class Genre {
        private String name;
    }
}   // 영화 ID로 상세 조회했을 때의 응답 구조
    // 안에 Genre 라는 하위 클래스가 포함되어 있음 (장르 이름 포함)
