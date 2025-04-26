package miniteam.moviesearch.dto;

import lombok.Data;

import java.util.List;

@Data       // @Data를 통해 getter setter toString 등 작성 불필요
public class MovieDto { // 검색 결과용 DTO
    private Long id;
    private String title;
    private String releaseDate;
    private String posterUrl;
    private String overview;
    private List<String> genres;    // 장르 (태그용)
}
