package miniteam.moviesearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

// OMDb API의 응답 JSON 구조와 1:1로 매핑되게 만든 클래스
// 검색을 농해 API를 호출하면 오는 JSON내용을 그래도 자바 객체로 바꾸기 위한 클래스
@Data
public class OmdbSearchResponse {
    //Search 배열 안에 있는 각 영화 객체를 표현한 클래스
    //JsonProperty는 JSON키와 자바 필드 이름이 다를 때 매핑해주는 역할 (OMDb는 대문자 키를 사용하기 때문에 이게 필요함)
    @JsonProperty("Search")
    private List<OmdbMovieItem> search;
    private String totalResults;
    private String Response;
}

@Data
class OmdbMovieItem {
    @JsonProperty("Title")
    private String title;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("omdbID")
    private String omdbID;

    @JsonProperty("Poster")
    private String poster;
}
