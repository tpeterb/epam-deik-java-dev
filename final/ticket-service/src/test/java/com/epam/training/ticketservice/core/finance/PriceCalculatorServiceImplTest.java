package com.epam.training.ticketservice.core.finance;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.finance.pricecomponent.PriceComponentService;
import com.epam.training.ticketservice.core.finance.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.seat.model.Seat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceCalculatorServiceImplTest {

    @Mock
    private PriceComponentService priceComponentServiceMock;

    @InjectMocks
    private PriceCalculatorServiceImpl underTest;

    private static final BookingDto BOOKING_DTO = new BookingDto(
            "Johnny English",
            "Avalanche",
            LocalDateTime.parse(
                    "2022-02-01 15:00",
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            List.of(new Seat(1,1),
                    new Seat(4, 5),
                    new Seat(7, 3),
                    new Seat(2, 7))
    );

    @Test
    void testCalculateBookingPriceShouldReturnAPositiveIntegerWhenTheBasePriceIsDefined() {
        // Given
        final ScreeningDto screeningDto = new ScreeningDto(
                BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartOfScreening()
        );
        when(priceComponentServiceMock.getPriceComponentsByMovie(BOOKING_DTO.getMovieTitle())).thenReturn(
                List.of(
                        new PriceComponentDto("MovieComponent1", 350),
                        new PriceComponentDto("MovieComponent2", 500)
                )
        );
        when(priceComponentServiceMock.getPriceComponentsByRoom(BOOKING_DTO.getRoomName())).thenReturn(
                List.of(
                        new PriceComponentDto("RoomComponent1", 120),
                        new PriceComponentDto("RoomComponent2", 80),
                        new PriceComponentDto("RoomComponent3", 210)
                )
        );
        when(priceComponentServiceMock.getPriceComponentsByScreening(screeningDto)).thenReturn(
                List.of(
                        new PriceComponentDto("ScreeningComponent1", 310)
        ));
        when(priceComponentServiceMock.getBasePriceComponent()).thenReturn(Optional.of(
                new PriceComponentDto("BasePriceComponent", 1600)
        ));

        int expected = 12680;

        // When
        int actual = underTest.calculateBookingPrice(BOOKING_DTO);

        // Then
        assertEquals(expected, actual);
        assertTrue(actual > 0);
        verify(priceComponentServiceMock).getBasePriceComponent();
        verify(priceComponentServiceMock).getPriceComponentsByMovie("Johnny English");
        verify(priceComponentServiceMock).getPriceComponentsByRoom("Avalanche");
        verify(priceComponentServiceMock).getPriceComponentsByScreening(screeningDto);
    }

    @Test
    void testCalculateBookingPriceShouldReturnMinusOneWhenTheBasePriceIsNotDefined() {
        // Given
        final ScreeningDto screeningDto = new ScreeningDto(
                BOOKING_DTO.getMovieTitle(),
                BOOKING_DTO.getRoomName(),
                BOOKING_DTO.getStartOfScreening()
        );
        when(priceComponentServiceMock.getPriceComponentsByMovie(BOOKING_DTO.getMovieTitle())).thenReturn(
                List.of(
                        new PriceComponentDto("MovieComponent1", 350),
                        new PriceComponentDto("MovieComponent2", 500)
                )
        );
        when(priceComponentServiceMock.getPriceComponentsByRoom(BOOKING_DTO.getRoomName())).thenReturn(
                List.of(
                        new PriceComponentDto("RoomComponent1", 120),
                        new PriceComponentDto("RoomComponent2", 80),
                        new PriceComponentDto("RoomComponent3", 210)
                )
        );
        when(priceComponentServiceMock.getPriceComponentsByScreening(screeningDto)).thenReturn(
                List.of(
                        new PriceComponentDto("ScreeningComponent1", 310)
                )
        );
        when(priceComponentServiceMock.getBasePriceComponent()).thenReturn(Optional.empty());

        int expected = -1;

        // When
        int actual = underTest.calculateBookingPrice(BOOKING_DTO);

        // Then
        assertEquals(expected, actual);
        assertEquals(actual, -1);
        verify(priceComponentServiceMock).getBasePriceComponent();
        verify(priceComponentServiceMock).getPriceComponentsByMovie("Johnny English");
        verify(priceComponentServiceMock).getPriceComponentsByRoom("Avalanche");
        verify(priceComponentServiceMock).getPriceComponentsByScreening(screeningDto);
    }

}