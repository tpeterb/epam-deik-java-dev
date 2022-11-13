package com.epam.training.ticketservice.core.booking.persistence.entity;

import com.epam.training.ticketservice.core.Seat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class BookingId implements Serializable {

    private String movieTitle;

    private String roomName;

    private LocalDateTime startOfScreening;

    //private List<Seat> bookedSeats;

    private String username;

}
