package com.epam.training.ticketservice.core.finance;

import com.epam.training.ticketservice.core.booking.model.BookingDto;

public interface PriceCalculatorService {

    int calculateBookingPrice(BookingDto bookingDto);

}
