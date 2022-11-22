package com.epam.training.ticketservice.core.booking;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.user.model.UserDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingService {

    void createBooking(BookingDto bookingDto);

    List<BookingDto> retrieveBookingsForUser(UserDto userDto);

    List<BookingDto> getAllBookingsForScreening(String movieTitle, String roomName, LocalDateTime startOfScreening);

}
