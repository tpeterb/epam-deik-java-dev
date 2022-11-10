package ui.command;

import core.movie.MovieService;
import core.movie.MovieServiceImpl;
import core.movie.model.MovieDto;
import core.movie.persistence.entity.Movie;
import core.movie.persistence.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class MovieCommand {

    private final MovieService movieService;

    @ShellMethod(key = "create movie")
    public MovieDto createMovie(String title, String genre, int lengthInMinutes) {
        MovieDto movieDto = new MovieDto(title, genre, lengthInMinutes);
        movieService.createMovie(movieDto);
        return movieDto;
    }

    @ShellMethod(key = "update movie")
    public MovieDto updateMovie(String title, String genre, int lengthInMinutes) {
        MovieDto movieDto = new MovieDto(title, genre, lengthInMinutes);
        movieService.updateMovie(movieDto);
        return movieDto;
    }

    @ShellMethod(key = "delete movie")
    public void deleteMovie(String title) {
        movieService.deleteMovie(title);
    }

    @ShellMethod(key = "list movies")
    public String listMovies() {
        List<MovieDto> movieDtos = movieService.getMovieList();
        if (movieDtos.isEmpty()) {
            return "There are no movies at the moment";
        } else {
            return movieDtos.toString();
        }
    }

}
