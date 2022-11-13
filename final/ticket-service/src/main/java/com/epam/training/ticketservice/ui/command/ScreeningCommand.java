package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningId;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommand {

    private final ScreeningService screeningService;

    private final UserService userService;

    @ShellMethod(key = "create screening")
    @ShellMethodAvailability("isAvailable")
    public String createScreening(ScreeningId screeningId) {
        //TODO

        LocalDateTime startOfScreeningToCreate = screeningId.getStartOfScreening();
        List<ScreeningDto> screenings = screeningService.getScreeningListByRoom(screeningId.getRoomName());
        for (var screening : screenings) {
            if (screeningService.areScreeningsColliding(screening, new ScreeningDto(
                            screeningId.getMovieTitle(),
                            screeningId.getRoomName(),
                            screeningId.getStartOfScreening()))) {
                return "There is an overlapping screening";
            } else if (true) {
                //TODO
                return "This would start in the break period after another screening in this room";
            }
        }

        ScreeningDto screeningDto = new ScreeningDto(
                screeningId.getMovieTitle(),
                screeningId.getRoomName(),
                screeningId.getStartOfScreening()
        );
        screeningService.createScreening(screeningDto);
        return "Screening successfully created!";
    }

    @ShellMethod(key = "delete screening")
    @ShellMethodAvailability("isAvailable")
    public void deleteScreening(ScreeningId screeningId) {
        ScreeningDto screeningDto = new ScreeningDto(
                screeningId.getMovieTitle(),
                screeningId.getRoomName(),
                screeningId.getStartOfScreening()
        );
        screeningService.deleteScreening(screeningDto);
    }

    @ShellMethod(key = "list screenings")
    public String listScreenings() {
        List<ScreeningDto> screenings = screeningService.getScreeningList();
        if (screenings.isEmpty()) {
            return "There are no screenings";
        }
        return screenings.toString();
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }

}
