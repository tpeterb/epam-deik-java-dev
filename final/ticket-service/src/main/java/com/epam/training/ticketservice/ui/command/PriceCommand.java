package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.finance.PriceCalculatorService;
import com.epam.training.ticketservice.core.finance.pricecomponent.PriceComponentService;
import com.epam.training.ticketservice.core.finance.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.seat.model.Seat;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
@AllArgsConstructor
public class PriceCommand {

    private final UserService userService;

    private final PriceComponentService priceComponentService;

    private final PriceCalculatorService priceCalculatorService;

    @ShellMethod(key = "update base price")
    @ShellMethodAvailability("isAvailable")
    public int updateBasePrice(int newBasePrice) {
        priceComponentService.updateBasePriceComponent(newBasePrice);
        return newBasePrice;
    }

    @ShellMethod(key = "create price component")
    @ShellMethodAvailability("isAvailable")
    public PriceComponentDto createPriceComponent(String priceComponentName, int valueOfPriceComponent) {
        PriceComponentDto priceComponentDto = new PriceComponentDto(priceComponentName, valueOfPriceComponent);
        priceComponentService.createPriceComponent(priceComponentDto);
        return priceComponentDto;
    }

    @ShellMethod(key = "attach price component to room")
    @ShellMethodAvailability("isAvailable")
    public void attachPriceComponentToRoom(String priceComponentName, String roomName) {
        Optional<PriceComponentDto> priceComponentDto =
                priceComponentService.getPriceComponentByName(priceComponentName);
        if (priceComponentDto.isPresent()) {
            priceComponentService.addPriceComponentToRoom(priceComponentDto.get(), roomName);
        }
    }

    @ShellMethod(key = "attach price component to movie")
    @ShellMethodAvailability("isAvailable")
    public void attachPriceComponentToMovie(String priceComponentName, String movieTitle) {
        Optional<PriceComponentDto> priceComponentDto =
                priceComponentService.getPriceComponentByName(priceComponentName);
        if (priceComponentDto.isPresent()) {
            priceComponentService.addPriceComponentToMovie(priceComponentDto.get(), movieTitle);
        }
    }

    @ShellMethod(key = "attach price component to screening")
    @ShellMethodAvailability("isAvailable")
    public void attachPriceComponentToScreening(String priceComponentName,
                                                String movieTitle,
                                                String roomName,
                                                String startOfScreening) {
        LocalDateTime startOfScreeningDateAndTime = LocalDateTime.parse(startOfScreening,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Optional<PriceComponentDto> priceComponentDto =
                priceComponentService.getPriceComponentByName(priceComponentName);
        if (priceComponentDto.isPresent()) {
            priceComponentService.addPriceComponentToScreening(priceComponentDto.get(), new ScreeningDto(
                    movieTitle, roomName, startOfScreeningDateAndTime));
        }
    }

    @ShellMethod(key = "show price for")
    public String showPriceFor(String movieTitle,
                             String roomName,
                             String startOfScreening,
                             String seats) {
        LocalDateTime startOfScreeningDateAndTime = LocalDateTime.parse(startOfScreening,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        List<String> splitSeats = List.of(seats.split(" "));
        List<Seat> bookedSeats = splitSeats.stream()
                .map(seat -> new Seat(Integer.valueOf(seat.split(",")[0]),
                        Integer.valueOf(seat.split(",")[1])))
                .collect(Collectors.toList());
        BookingDto bookingDto = new BookingDto(
                movieTitle,
                roomName,
                startOfScreeningDateAndTime,
                bookedSeats
        );
        int price = priceCalculatorService.calculateBookingPrice(bookingDto);
        return "The price for this booking would be " + Integer.valueOf(price).toString() + " HUF";
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }

}
