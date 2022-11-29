package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
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

    private final MovieService movieService;

    private final RoomService roomService;

    @Override
    public void createScreening(ScreeningDto screeningDto) {
        Screening screening = new Screening(
                screeningDto.getMovieTitle(),
                screeningDto.getRoomName(),
                screeningDto.getStartOfScreening()
        );
        screeningRepository.save(screening);
    }

    @Override
    public void deleteScreening(ScreeningDto screeningDto) {
        Optional<Screening> screening = screeningRepository.findByMovieTitleAndRoomNameAndStartOfScreening(
                        screeningDto.getMovieTitle(),
                        screeningDto.getRoomName(),
                        screeningDto.getStartOfScreening()
                );
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
        List<Screening> screenings = screeningRepository.findByRoomName(roomName);
        List<ScreeningDto> roomScreenings = screenings.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
        return roomScreenings;
    }

    @Override
    public Optional<ScreeningDto> getScreeningByMovieTitleAndRoomNameAndStartOfScreening(
            String movieTitle,
            String roomName,
            LocalDateTime startOfScreening) {
        Optional<Screening> screening = screeningRepository.findByMovieTitleAndRoomNameAndStartOfScreening(
                movieTitle,
                roomName,
                startOfScreening);
        if (screening.isPresent()) {
            return Optional.of(new ScreeningDto(
                    screening.get().getMovieTitle(),
                    screening.get().getRoomName(),
                    screening.get().getStartOfScreening()));
        }
        return Optional.empty();
    }

    @Override
    public boolean areScreeningsColliding(ScreeningDto screeningDto1, ScreeningDto screeningDto2) {
        LocalDateTime screeningDto1StartOfScreening = screeningDto1.getStartOfScreening();
        LocalDateTime screeningDto2StartOfScreening = screeningDto2.getStartOfScreening();
        Optional<MovieDto> movie1 = movieService.getMovieByTitle(screeningDto1.getMovieTitle());
        Optional<MovieDto> movie2 = movieService.getMovieByTitle(screeningDto2.getMovieTitle());
        Optional<RoomDto> room1 = roomService.getRoomByName(screeningDto1.getRoomName());
        Optional<RoomDto> room2 = roomService.getRoomByName(screeningDto2.getRoomName());
        if (movie1.isEmpty() || movie2.isEmpty() || room1.isEmpty() || room2.isEmpty()) {
            return false;
        }
        if (!room1.get().getName().equals(room2.get().getName())) {
            return false;
        }
        int movie1LengthInMinutes = movie1.get().getLengthInMinutes();
        int movie2LengthInMinutes = movie2.get().getLengthInMinutes();
        if ((screeningDto1StartOfScreening.isBefore(
                screeningDto2StartOfScreening.plusMinutes(movie2LengthInMinutes))
            && screeningDto1StartOfScreening.plusMinutes(movie1LengthInMinutes).isAfter(
                    screeningDto2StartOfScreening))
            || (screeningDto2StartOfScreening.isBefore(
                    screeningDto1StartOfScreening.plusMinutes(movie1LengthInMinutes))
                && screeningDto2StartOfScreening.plusMinutes(movie2LengthInMinutes).isAfter(
                        screeningDto1StartOfScreening))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isScreeningCollidingWithTenMinutesBreakPeriod(
            ScreeningDto screeningDtoWithBreakAfter,
            ScreeningDto screeningDtoWhichMightFallInBreakPeriod) {
        LocalDateTime screeningWithBreakAfterStartOfScreening = screeningDtoWithBreakAfter.getStartOfScreening();
        LocalDateTime screeningWhichMightFallInBreakPeriodStartOfScreening =
                screeningDtoWhichMightFallInBreakPeriod.getStartOfScreening();
        Optional<MovieDto> movieWithBreakAfter = movieService.getMovieByTitle(
                screeningDtoWithBreakAfter.getMovieTitle());
        Optional<MovieDto> movieWhichMightFallInBreakPeriod = movieService.getMovieByTitle(
                screeningDtoWhichMightFallInBreakPeriod.getMovieTitle());
        Optional<RoomDto> roomWithBreakAfter = roomService.getRoomByName(screeningDtoWithBreakAfter.getRoomName());
        Optional<RoomDto> roomWhichMightFallInBreakPeriod = roomService.getRoomByName(
                screeningDtoWhichMightFallInBreakPeriod.getRoomName());
        if (movieWithBreakAfter.isEmpty()
                || movieWhichMightFallInBreakPeriod.isEmpty()
                || roomWithBreakAfter.isEmpty()
                || roomWhichMightFallInBreakPeriod.isEmpty()) {
            return false;
        }
        if (!roomWithBreakAfter.get().getName().equals(
                roomWhichMightFallInBreakPeriod.get().getName())) {
            return false;
        }
        int movieWithBreakAfterLengthInMinutes = movieWithBreakAfter.get().getLengthInMinutes();
        int movieWhichMightFallInBreakPeriodLengthInMinutes =
                movieWhichMightFallInBreakPeriod.get().getLengthInMinutes();
        if (screeningWhichMightFallInBreakPeriodStartOfScreening.isBefore(
                screeningWithBreakAfterStartOfScreening.plusMinutes(
                        movieWithBreakAfterLengthInMinutes).plusMinutes(10)
        ) && screeningWhichMightFallInBreakPeriodStartOfScreening.plusMinutes(
                movieWhichMightFallInBreakPeriodLengthInMinutes).isAfter(
                screeningWithBreakAfterStartOfScreening.plusMinutes(movieWithBreakAfterLengthInMinutes)
        )) {
            return true;
        }
        return false;
    }

    private ScreeningDto convertEntityToDto(Screening screening) {
        return new ScreeningDto(
                screening.getMovieTitle(),
                screening.getRoomName(),
                screening.getStartOfScreening()
        );
    }

}
