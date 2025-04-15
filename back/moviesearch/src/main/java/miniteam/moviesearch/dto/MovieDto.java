package miniteam.moviesearch.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieDto {
    private String omdbID;
    private String title;
    private String year;
    private String poster;
    private List<String> genres;    // 장르 (태그용)
}
