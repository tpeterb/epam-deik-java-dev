package core.movie;

import core.movie.model.MovieDto;
import core.movie.persistence.entity.Movie;
import core.movie.persistence.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public void createMovie(MovieDto movieDto) {
        Movie movie = new Movie(
                movieDto.getTitle(),
                movieDto.getGenre(),
                movieDto.getLengthInMinutes()
        );
        movieRepository.save(movie);
    }

    @Override
    public void updateMovie(MovieDto movieDto) {
        Optional<Movie> movie = movieRepository.findByTitle(movieDto.getTitle());
        if (movie.isPresent()) {
            Movie movieValue = movie.get();
            movieValue.setGenre(movieDto.getGenre());
            movieValue.setLengthInMinutes(movieDto.getLengthInMinutes());
        }
    }

    @Override
    public void deleteMovie(String title) {
        Optional<Movie> movie = movieRepository.findByTitle(title);
        if (movie.isPresent()) {
            movieRepository.delete(movie.get());
        }
    }

    @Override
    public List<MovieDto> getMovieList() {
        return movieRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }


    private MovieDto convertEntityToDto(Movie movie) {
        return new MovieDto(
                movie.getTitle(),
                movie.getGenre(),
                movie.getLengthInMinutes());
    }

    private Movie convertDtoToEntity(MovieDto movieDto) {
        return new Movie(
                movieDto.getTitle(),
                movieDto.getGenre(),
                movieDto.getLengthInMinutes()
        );
    }

}
