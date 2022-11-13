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
public class PriceCommand {

    private final UserService userService;

    @ShellMethod(key = "update base price")
    @ShellMethodAvailability("isAvailable")
    public void updateBasePrice(int newBasePrice) {

    }

    @ShellMethod(key = "create price component")
    @ShellMethodAvailability("isAvailable")
    public void createPriceComponent(String priceComponentName, int valueOfPriceComponent) {

    }

    @ShellMethod(key = "attach price component to room")
    @ShellMethodAvailability("isAvailable")
    public void attachPriceComponentToRoom(String priceComponentName, String roomName) {

    }

    @ShellMethod(key = "attach price component to movie")
    @ShellMethodAvailability("isAvailable")
    public void attachPriceComponentToMovie(String priceComponentName, String movieTitle) {

    }

    @ShellMethod(key = "attach price component to screening")
    @ShellMethodAvailability("isAvailable")
    public void attachPriceComponentToScreening(String priceComponentName,
                                                String movieTitle,
                                                String roomName,
                                                LocalDateTime startOfScreening) {

    }

    @ShellMethod(key = "show price for")
    public void showPriceFor(String movieTitle,
                             String roomName,
                             LocalDateTime startOfScreening,
                             List<Seat> seats) {

    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }

}
