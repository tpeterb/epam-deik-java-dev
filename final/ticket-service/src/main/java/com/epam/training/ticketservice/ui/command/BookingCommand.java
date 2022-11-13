package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.Seat;
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
public class BookingCommand {

    private final UserService userService;

    @ShellMethod(key = "book")
    @ShellMethodAvailability("isAvailable")
    public void book(String movieTitle,
                     String roomName,
                     LocalDateTime startOfScreening,
                     List<Seat> seats) {

    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        if (user.isPresent() && user.get().getRole() == User.Role.USER) {
            return Availability.available();
        }
        return Availability.unavailable("You are either not logged in or you are an admin!");
    }

}
