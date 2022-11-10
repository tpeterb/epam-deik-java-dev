package core.booking.persistence.entity;

import core.room.persistence.entity.Room;
import core.Seat;
import core.movie.persistence.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "foglalas")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String movieTitle;

    private String roomName;

    private LocalDateTime startOfScreening;

    private List<Seat> bookedSeats;

}
