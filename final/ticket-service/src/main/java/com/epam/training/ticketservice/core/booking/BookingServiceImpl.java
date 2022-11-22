package com.epam.training.ticketservice.core.booking;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final UserService userService;

    private final UserRepository userRepository;

    @Override
    public void createBooking(BookingDto bookingDto) {
        Optional<User> user = userRepository.findByUsername(userService.describe().get().getUsername());
        if (user.isPresent()) {
            Booking booking = new Booking(
                    bookingDto.getMovieTitle(),
                    bookingDto.getRoomName(),
                    bookingDto.getStartOfScreening(),
                    bookingDto.getPrice(),
                    bookingDto.getBookedSeats(),
                    user.get()
            );
            bookingRepository.save(booking);
        }
    }

    @Override
    public List<BookingDto> retrieveBookingsForUser(UserDto userDto) {
        List<Booking> userBookings = bookingRepository.findAllByUserUsername(userDto.getUsername());
        List<BookingDto> bookingDtos = userBookings.stream()
                .map(this::convertFromEntityToDto)
                .collect(Collectors.toList());
        return bookingDtos;
    }

    @Override
    public List<BookingDto> getAllBookingsForScreening(String movieTitle,
                                                       String roomName,
                                                       LocalDateTime startOfScreening) {
        List<Booking> bookings = bookingRepository.findAllByMovieTitleAndRoomNameAndStartOfScreening(
                movieTitle,
                roomName,
                startOfScreening
        );
        return bookings.stream()
                .map(this::convertFromEntityToDto)
                .collect(Collectors.toList());
    }

    private BookingDto convertFromEntityToDto(Booking booking) {
        return new BookingDto(booking.getMovieTitle(),
                booking.getRoomName(),
                booking.getStartOfScreening(),
                booking.getPrice(),
                booking.getBookedSeats());
    }

}
