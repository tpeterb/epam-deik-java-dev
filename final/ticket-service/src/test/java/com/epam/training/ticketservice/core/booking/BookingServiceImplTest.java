package com.epam.training.ticketservice.core.booking;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.seat.model.Seat;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    private final BookingRepository bookingRepositoryMock = mock(BookingRepository.class);

    private final UserService userServiceMock = mock(UserService.class);

    private final UserRepository userRepositoryMock = mock(UserRepository.class);

    private final BookingService underTest = new BookingServiceImpl(
            bookingRepositoryMock,
            userServiceMock,
            userRepositoryMock
    );

    private static final LocalDateTime SCREENING_DATE = LocalDateTime.parse(
            "2022-11-25 16:00",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    );

    private static final User LOGGED_IN_USER = new User(
            "username",
            "password",
            User.Role.USER
    );

    private static final UserDto LOGGED_IN_USER_DTO = new UserDto(
            LOGGED_IN_USER.getUsername(),
            LOGGED_IN_USER.getRole()
    );

    private static final Booking BOOKING_ENTITY = new Booking(
            "Johnny English",
            "Pedersoli",
            SCREENING_DATE,
            3000,
            List.of(
                    new Seat(5, 5),
                    new Seat(2, 9)
            ),
            LOGGED_IN_USER
    );

    private static final BookingDto BOOKING_DTO = new BookingDto(
            BOOKING_ENTITY.getMovieTitle(),
            BOOKING_ENTITY.getRoomName(),
            BOOKING_ENTITY.getStartOfScreening(),
            BOOKING_ENTITY.getPrice(),
            BOOKING_ENTITY.getBookedSeats()
    );

    @Test
    void testCreateBookingShouldStoreTheGivenBookingWhenTheBookingIsValidAndTheUserIsLoggedIn() {
        // Given
        when(userServiceMock.describe()).thenReturn(
                Optional.of(LOGGED_IN_USER_DTO)
        );
        when(userRepositoryMock.findByUsername(LOGGED_IN_USER_DTO.getUsername())).thenReturn(
                Optional.of(LOGGED_IN_USER)
        );
        when(bookingRepositoryMock.save(BOOKING_ENTITY)).thenReturn(BOOKING_ENTITY);

        // When
        underTest.createBooking(BOOKING_DTO);

        // Then
        verify(userServiceMock).describe();
        verify(userRepositoryMock).findByUsername(LOGGED_IN_USER_DTO.getUsername());
        verify(bookingRepositoryMock).save(BOOKING_ENTITY);
    }

    @Test
    void testRetrieveBookingsForUserShouldReturnAListWithOneElement() {
        // Given
        when(bookingRepositoryMock.findAllByUserUsername(LOGGED_IN_USER_DTO.getUsername()))
                .thenReturn(List.of(BOOKING_ENTITY));
        List<BookingDto> expected = List.of(BOOKING_DTO);

        // When
        List<BookingDto> actual = underTest.retrieveBookingsForUser(LOGGED_IN_USER_DTO);

        // Then
        assertEquals(expected, actual);
        assertEquals(1, actual.size());
        verify(bookingRepositoryMock).findAllByUserUsername(LOGGED_IN_USER_DTO.getUsername());
    }

    @Test
    void testGetAllBookingsForScreeningShouldReturnAListWithOneElement() {
        // Given
        final String movieTitle = "Johnny English";
        final String roomName = "Pedersoli";
        when(bookingRepositoryMock.findAllByMovieTitleAndRoomNameAndStartOfScreening(
                movieTitle,
                roomName,
                SCREENING_DATE
        )).thenReturn(List.of(BOOKING_ENTITY));
        List<BookingDto> expected = List.of(BOOKING_DTO);

        // When
        List<BookingDto> actual = underTest.getAllBookingsForScreening(
                movieTitle,
                roomName,
                SCREENING_DATE
        );

        // Then
        assertEquals(expected, actual);
        assertEquals(1, actual.size());
        verify(bookingRepositoryMock).findAllByMovieTitleAndRoomNameAndStartOfScreening(
                movieTitle,
                roomName,
                SCREENING_DATE
        );
    }
}