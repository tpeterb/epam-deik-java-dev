package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningId;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;

    private final MovieRepository movieRepository;

    @Override
    public void createScreening(ScreeningDto screeningDto) {
        Optional<Movie> movie = movieRepository.findByTitle(screeningDto.getMovieTitle());
        if (!movie.isEmpty()) {
            Screening screening = new Screening(new ScreeningId(
                    screeningDto.getMovieTitle(),
                    screeningDto.getRoomName(),
                    screeningDto.getStartOfScreening()
            ));
            screeningRepository.save(screening);
        }
    }

    @Override
    public void deleteScreening(ScreeningDto screeningDto) {
        Optional<Screening> screening = screeningRepository.findByScreeningId(
                new ScreeningId(
                        screeningDto.getMovieTitle(),
                        screeningDto.getRoomName(),
                        screeningDto.getStartOfScreening()
                ));
        if (screening.isPresent()) {
            screeningRepository.delete(screening.get());
        }
    }

    @Override
    public List<ScreeningDto> getScreeningList() {
        return screeningRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScreeningDto> getScreeningListByRoom(String roomName) {
        List<Screening> screenings = screeningRepository.findByScreeningId_RoomName(roomName);
        List<ScreeningDto> roomScreenings = screenings.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
        return roomScreenings;
    }

    @Override
    public boolean areScreeningsColliding(ScreeningDto screeningDto1, ScreeningDto screeningDto2) {
        LocalDateTime screeningDto1StartOfScreening = screeningDto1.getStartOfScreening();
        LocalDateTime screeningDto2StartOfScreening = screeningDto2.getStartOfScreening();
        Optional<Movie> movie1 = movieRepository.findByTitle(screeningDto1.getMovieTitle());
        Optional<Movie> movie2 = movieRepository.findByTitle(screeningDto2.getMovieTitle());
        if (movie1.isEmpty() || movie2.isEmpty()) {
            return false;
        }
        int movie1LengthInMinutes = movie1.get().getLengthInMinutes();
        int movie2LengthInMinutes = movie2.get().getLengthInMinutes();
        if ((screeningDto1StartOfScreening.isBefore(screeningDto2StartOfScreening)
            && screeningDto1StartOfScreening.plusMinutes(movie1LengthInMinutes).isBefore(screeningDto2StartOfScreening))
            || (screeningDto2StartOfScreening.isBefore(screeningDto1StartOfScreening)
                && screeningDto2StartOfScreening.plusMinutes(movie2LengthInMinutes).isBefore(screeningDto1StartOfScreening))) {
            return true;
        }
        return false;
    }

    private ScreeningDto convertEntityToDto(Screening screening) {
        ScreeningId screeningId = screening.getScreeningId();
        return new ScreeningDto(
                screeningId.getMovieTitle(),
                screeningId.getRoomName(),
                screeningId.getStartOfScreening()
        );
    }

}
