package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommand {

    private final ScreeningService screeningService;

    private final UserService userService;

    private final MovieService movieService;

    private final RoomService roomService;

    @ShellMethod(key = "create screening")
    @ShellMethodAvailability("isAvailable")
    public String createScreening(String movieTitle, String roomName, String startOfScreening) {
        LocalDateTime startOfScreeningDateAndTime = LocalDateTime.parse(startOfScreening,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        ScreeningDto screeningDtoToCreate = new ScreeningDto(movieTitle, roomName, startOfScreeningDateAndTime);
        List<ScreeningDto> screenings = screeningService.getScreeningListByRoom(roomName);
        for (var screening : screenings) {
            if (screeningService.areScreeningsColliding(screening, screeningDtoToCreate)) {
                return "There is an overlapping screening";
            } else if (screeningService.isScreeningCollidingWithTenMinutesBreakPeriod(
                    new ScreeningDto(
                            screening.getMovieTitle(),
                            screening.getRoomName(),
                            screening.getStartOfScreening()),
                            screeningDtoToCreate)) {
                return "This would start in the break period after another screening in this room";
            }
        }
        Optional<MovieDto> movie = movieService.getMovieByTitle(movieTitle);
        Optional<RoomDto> room = roomService.getRoomByName(roomName);
        if (movie.isPresent() && room.isPresent()) {
            screeningService.createScreening(screeningDtoToCreate);
            return "Screening successfully created!";
        }
        return "There's no such movie or room!";
    }

    @ShellMethod(key = "delete screening")
    @ShellMethodAvailability("isAvailable")
    public void deleteScreening(String movieTitle, String roomName, String startOfScreening) {
        LocalDateTime startOfScreeningDateAndTime = LocalDateTime.parse(startOfScreening,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        ScreeningDto screeningDto = new ScreeningDto(
                movieTitle,
                roomName,
                startOfScreeningDateAndTime
        );
        screeningService.deleteScreening(screeningDto);
    }

    @ShellMethod(key = "list screenings")
    public String listScreenings() {
        List<ScreeningDto> screenings = screeningService.getScreeningList();
        if (screenings.isEmpty()) {
            return "There are no screenings";
        }
        return convertScreeningListToOutputForm(screenings);
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }

    private String convertScreeningListToOutputForm(List<ScreeningDto> screenings) {
        String outputString = "";
        Collections.reverse(screenings); // Due to the acceptance tests
        for (var screening : screenings) {
            MovieDto movie = movieService.getMovieByTitle(screening.getMovieTitle()).get();
            outputString += movie.toString();
            outputString += ", screened in room " + screening.getRoomName();
            outputString += ", at "
                    + screening.getStartOfScreening().format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    + "\n";
        }
        outputString = outputString.trim();
        return outputString;
    }

}
