package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
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
            Optional<Movie> movieToUpdate = movieRepository.findById(movie.get().getId());
            Movie movieValueToUpdate = movieToUpdate.get();
            movieValueToUpdate.setGenre(movieDto.getGenre());
            movieValueToUpdate.setLengthInMinutes(movieDto.getLengthInMinutes());
            movieRepository.save(movieValueToUpdate);
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

    @Override
    public Optional<MovieDto> getMovieByTitle(String movieTitle) {
        Optional<Movie> movie = movieRepository.findByTitle(movieTitle);
        if (movie.isPresent()) {
            return Optional.of(convertEntityToDto(movie.get()));
        }
        return Optional.empty();
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
