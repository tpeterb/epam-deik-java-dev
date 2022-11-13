package com.epam.training.ticketservice.core.booking.model;

import com.epam.training.ticketservice.core.Seat;

import java.time.LocalDateTime;
import java.util.List;

public class BookingDto {

    private String movieTitle;

    private String roomName;

    private LocalDateTime startOfScreening;

    private List<Seat> bookedSeats;

}
