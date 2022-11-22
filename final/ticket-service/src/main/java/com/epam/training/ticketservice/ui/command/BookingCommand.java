package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.finance.PriceCalculatorService;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
@AllArgsConstructor
public class BookingCommand {

    private final UserService userService;

    private final ScreeningService screeningService;

    private final RoomService roomService;

    private final BookingService bookingService;

    private final MovieService movieService;

    private final PriceCalculatorService priceCalculatorService;

    @ShellMethod(key = "book")
    @ShellMethodAvailability("isAvailable")
    public String book(String movieTitle,
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
        Optional<ScreeningDto> screening =
                screeningService.getScreeningByMovieTitleAndRoomNameAndStartOfScreening(
                    movieTitle,
                    roomName,
                    startOfScreeningDateAndTime);
        Optional<RoomDto> room = roomService.getRoomByName(roomName);
        Optional<MovieDto> movie = movieService.getMovieByTitle(movieTitle);
        if (room.isPresent() && movie.isPresent() && screening.isPresent()) {
            List<BookingDto> bookingsForScreening = bookingService.getAllBookingsForScreening(
                    movieTitle, roomName, startOfScreeningDateAndTime);
            for (var seat : bookedSeats) {
                if (!room.get().doesSeatExistInRoom(seat)) {
                    return "Seat " + seat.toString() + " does not exist in this room";
                }
                for (var booking : bookingsForScreening) {
                    if (booking.getBookedSeats().contains(seat)) {
                        return "Seat " + seat.toString() + " is already taken";
                    }
                }
            }
            BookingDto bookingDto = new BookingDto(
                    movieTitle,
                    roomName,
                    startOfScreeningDateAndTime,
                    bookedSeats
            );
            bookingDto.setPrice(priceCalculatorService.calculateBookingPrice(bookingDto));
            bookingService.createBooking(bookingDto);
            return bookingDto.toString();
        }

        return "";
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        if (user.isPresent() && user.get().getRole() == User.Role.USER) {
            return Availability.available();
        }
        return Availability.unavailable("You are either not logged in or you are an admin!");
    }

}
