package core.movie;

import core.movie.model.MovieDto;
import core.movie.persistence.entity.Movie;

import java.util.List;

public interface MovieService {

    void createMovie(MovieDto movieDto);

    void updateMovie(MovieDto movieDto);

    void deleteMovie(String title);

    List<MovieDto> getMovieList();

}
