package miniteam.moviesearch.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TmdbMovieItem {
    private Long id;
    private String title;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("genre_ids")
    private List<Integer> genreIds;

}
