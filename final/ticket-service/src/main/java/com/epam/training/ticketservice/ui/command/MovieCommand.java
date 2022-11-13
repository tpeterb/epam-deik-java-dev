package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class MovieCommand {

    private final MovieService movieService;
    private final UserService userService;

    @ShellMethod(key = "create movie")
    @ShellMethodAvailability("isAvailable")
    public MovieDto createMovie(String title, String genre, int lengthInMinutes) {
        MovieDto movieDto = new MovieDto(title, genre, lengthInMinutes);
        movieService.createMovie(movieDto);
        return movieDto;
    }

    @ShellMethod(key = "update movie")
    @ShellMethodAvailability("isAvailable")
    public MovieDto updateMovie(String title, String genre, int lengthInMinutes) {
        MovieDto movieDto = new MovieDto(title, genre, lengthInMinutes);
        movieService.updateMovie(movieDto);
        return movieDto;
    }

    @ShellMethod(key = "delete movie")
    @ShellMethodAvailability("isAvailable")
    public void deleteMovie(String title) {
        movieService.deleteMovie(title);
    }

    @ShellMethod(key = "list movies")
    public String listMovies() {
        List<MovieDto> movies = movieService.getMovieList();
        if (movies.isEmpty()) {
            return "There are no movies at the moment";
        }
        return movies.toString();
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }

}
