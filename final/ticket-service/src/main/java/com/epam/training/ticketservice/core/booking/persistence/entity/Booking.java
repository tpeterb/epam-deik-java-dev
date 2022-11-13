package com.epam.training.ticketservice.core.booking.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Data
@Table(name = "foglalas")
public class Booking {

    @EmbeddedId
    private BookingId bookingId;

}
