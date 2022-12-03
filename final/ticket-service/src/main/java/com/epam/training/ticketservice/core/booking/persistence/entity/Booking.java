package com.epam.training.ticketservice.core.booking.persistence.entity;

import com.epam.training.ticketservice.core.seat.model.Seat;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "foglalas")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "booking_id")
    private Integer id;

    private String movieTitle;

    private String roomName;

    private LocalDateTime startOfScreening;

    private Integer price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Seat> bookedSeats;

    @ManyToOne
    private User user;

    public Booking(String movieTitle,
                   String roomName,
                   LocalDateTime startOfScreening,
                   Integer price,
                   List<Seat> bookedSeats,
                   User user) {
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.startOfScreening = startOfScreening;
        this.price = price;
        this.bookedSeats = bookedSeats;
        this.user = user;
    }

}
