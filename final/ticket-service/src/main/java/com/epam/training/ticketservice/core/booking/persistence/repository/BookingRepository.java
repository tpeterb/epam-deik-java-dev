package com.epam.training.ticketservice.core.booking.persistence.repository;

import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Optional<Booking> findById(Integer id);

    List<Booking> findAllByUserUsername(String username);

    List<Booking> findAllByMovieTitleAndRoomNameAndStartOfScreening(String movieTitle,
                                                                    String roomName,
                                                                    LocalDateTime startOfScreening);

}
