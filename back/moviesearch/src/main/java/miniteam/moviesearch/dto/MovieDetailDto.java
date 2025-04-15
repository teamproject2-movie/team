package miniteam.moviesearch.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieDetailDto {   // 상세 정보용 DTO
    private Long id;
    private String title;
    private String releaseDate;
    private String overview;
    private String posterUrl;
    private List<String> genres;
}
