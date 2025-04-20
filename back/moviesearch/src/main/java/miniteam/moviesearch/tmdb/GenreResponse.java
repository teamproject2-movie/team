package miniteam.moviesearch.tmdb;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data // getter/setter, toString(), equals() 등 자동 생성
public class GenreResponse {

    @JsonProperty("genres") // TMDb API 응답 JSON 중 "genres" 배열을 List<Genre>로 매핑
    private List<Genre> genres;

    @Data
    public static class Genre { // 장르 하나하나를 표현하는 클래스 예) { "id" = 28, "name" = "Action"}
        private int id;
        private String name;
    } // GenreResponse.Genre 형태로 내부 클래스를 만듦
}
