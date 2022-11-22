package com.epam.training.ticketservice.core.finance;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.finance.pricecomponent.PriceComponentService;
import com.epam.training.ticketservice.core.finance.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PriceCalculatorServiceImpl implements PriceCalculatorService {

    private final PriceComponentService priceComponentService;

    @Override
    @Transactional
    public int calculateBookingPrice(BookingDto bookingDto) {
        List<PriceComponentDto> priceComponentsForMovie = priceComponentService.getPriceComponentsByMovie(
                        bookingDto.getMovieTitle()
        );
        List<PriceComponentDto> priceComponentsForRoom = priceComponentService.getPriceComponentsByRoom(
                bookingDto.getRoomName()
        );
        List<PriceComponentDto> priceComponentsForScreening = priceComponentService.getPriceComponentsByScreening(
                new ScreeningDto(
                    bookingDto.getMovieTitle(),
                    bookingDto.getRoomName(),
                    bookingDto.getStartOfScreening()
        ));
        Optional<PriceComponentDto> basePriceComponent = priceComponentService.getBasePriceComponent();
        int numberOfSeatsBooked = bookingDto.getBookedSeats().size();
        if (basePriceComponent.isPresent()) {
            int priceComponentSumPerSeat = 0;
            for (var priceComponent : priceComponentsForMovie) {
                priceComponentSumPerSeat += priceComponent.getAmount();
            }
            for (var priceComponent : priceComponentsForRoom) {
                priceComponentSumPerSeat += priceComponent.getAmount();
            }
            for (var priceComponent : priceComponentsForScreening) {
                priceComponentSumPerSeat += priceComponent.getAmount();
            }
            return (basePriceComponent.get().getAmount() + priceComponentSumPerSeat) * numberOfSeatsBooked;
        }
        return -1;
    }

}
