package com.epam.training.ticketservice.core.booking.model;

import com.epam.training.ticketservice.core.seat.model.Seat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class BookingDto {

    private String movieTitle;

    private String roomName;

    private LocalDateTime startOfScreening;

    private int price;

    private List<Seat> bookedSeats;

    public BookingDto(String movieTitle, String roomName, LocalDateTime startOfScreening, List<Seat> bookedSeats) {
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.startOfScreening = startOfScreening;
        this.bookedSeats = bookedSeats;
    }

    @Override
    public String toString() {
        String outputString = "";
        outputString += "Seats booked: ";
        for (var seat : bookedSeats) {
            outputString += (seat.toString() + ", ");
        }
        outputString = outputString.substring(0, outputString.length() - 2);
        outputString += "; the price for this booking is " + price + " HUF";
        return outputString;
    }

}
