package com.epam.training.ticketservice.core.screening.persistence.repository;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

    Optional<Screening> findById(Integer id);

    Optional<Screening> findByMovieTitleAndRoomNameAndStartOfScreening(
            String movieTitle,
            String roomName,
            LocalDateTime startOfScreening);

    List<Screening> findByRoomName(String roomName);

}
