package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.RoomServiceImpl;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScreeningServiceImplTest {

    private final ScreeningRepository screeningRepositoryMock = mock(ScreeningRepository.class);

    private final MovieService movieServiceMock = mock(MovieService.class);

    private final RoomService roomServiceMock = mock(RoomService.class);

    private final ScreeningService underTest = new ScreeningServiceImpl(
            screeningRepositoryMock,
            movieServiceMock,
            roomServiceMock
    );

    private static final LocalDateTime SCREENING_DATE = LocalDateTime.parse("2022-12-24 14:00",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

    private static final Screening ENTITY = new Screening(
            "Sátántangó",
            "Pedersoli",
            SCREENING_DATE
    );

    private static final ScreeningDto DTO = new ScreeningDto(
            "Sátántangó",
            "Pedersoli",
            SCREENING_DATE
    );

    @Test
    void testCreateScreeningShouldStoreTheGivenScreeningWhenInputScreeningIsValid() {
        // Given
        when(screeningRepositoryMock.save(ENTITY)).thenReturn(ENTITY);

        // When
        underTest.createScreening(DTO);

        // Then
        verify(screeningRepositoryMock).save(ENTITY);
    }

    @Test
    void testDeleteScreeningShouldDeleteScreeningWhenTheGivenScreeningExists() {
        // Given
        when(screeningRepositoryMock.findByMovieTitleAndRoomNameAndStartOfScreening(
                DTO.getMovieTitle(),
                DTO.getRoomName(),
                DTO.getStartOfScreening()
        )).thenReturn(Optional.of(ENTITY));

        // When
        underTest.deleteScreening(DTO);

        // Then
        verify(screeningRepositoryMock).delete(ENTITY);
    }

    @Test
    void testDeleteScreeningShouldNotDeleteAnythingWhenTheGivenScreeningDoesNotExist() {
        // Given
        when(screeningRepositoryMock.findByMovieTitleAndRoomNameAndStartOfScreening(
                DTO.getMovieTitle(),
                DTO.getRoomName(),
                DTO.getStartOfScreening()
        )).thenReturn(Optional.empty());

        // When
        underTest.deleteScreening(DTO);

        // Then
        verify(screeningRepositoryMock, never()).delete(ENTITY);
    }

    @Test
    void testGetScreeningListShouldReturnAListWithOneElement() {
        // Given
        when(screeningRepositoryMock.findAll()).thenReturn(List.of(ENTITY));
        List<ScreeningDto> expected = List.of(DTO);

        // When
        List<ScreeningDto> actual = underTest.getScreeningList();

        // Then
        assertEquals(expected, actual);
        assertFalse(actual.isEmpty());
        verify(screeningRepositoryMock).findAll();
    }

    @Test
    void testGetScreeningListByRoomShouldReturnAListWithOneElementWhenTheGivenRoomExists() {
        // Given
        when(screeningRepositoryMock.findByRoomName("Pedersoli")).thenReturn(List.of(ENTITY));
        List<ScreeningDto> expected = List.of(DTO);

        // When
        List<ScreeningDto> actual = underTest.getScreeningListByRoom("Pedersoli");

        // Then
        assertEquals(expected, actual);
        assertFalse(actual.isEmpty());
        verify(screeningRepositoryMock).findByRoomName("Pedersoli");
    }

    @Test
    void testGetScreeningListByRoomShouldReturnAnEmptyListWhenTheGivenRoomDoesNotExist() {
        // Given
        when(screeningRepositoryMock.findByRoomName("someRandomRoom")).thenReturn(List.of());
        List<ScreeningDto> expected = List.of();

        // When
        List<ScreeningDto> actual = underTest.getScreeningListByRoom("someRandomRoom");

        // Then
        assertEquals(expected, actual);
        assertTrue(actual.isEmpty());
        verify(screeningRepositoryMock).findByRoomName("someRandomRoom");
    }

    @Test
    void testGetScreeningByMovieTitleAndRoomNameAndStartOfScreeningShouldReturnANonEmptyOptionalWhenTheGivenScreeningExists() {
        // Given
        when(screeningRepositoryMock.findByMovieTitleAndRoomNameAndStartOfScreening(
                "Sátántangó",
                "Pedersoli",
                SCREENING_DATE
        )).thenReturn(Optional.of(ENTITY));
        Optional<ScreeningDto> expected = Optional.of(DTO);

        // When
        Optional<ScreeningDto> actual = underTest.getScreeningByMovieTitleAndRoomNameAndStartOfScreening(
                "Sátántangó",
                "Pedersoli",
                SCREENING_DATE
        );

        // Then
        assertEquals(expected, actual);
        assertFalse(actual.isEmpty());
        verify(screeningRepositoryMock).findByMovieTitleAndRoomNameAndStartOfScreening(
                "Sátántangó",
                "Pedersoli",
                SCREENING_DATE
        );
    }

    @Test
    void testGetScreeningByMovieTitleAndRoomNameAndStartOfScreeningShouldReturnAnEmptyOptionalWhenTheGivenScreeningDoesNotExist() {
        // Given
        when(screeningRepositoryMock.findByMovieTitleAndRoomNameAndStartOfScreening(
                "Spirited Away",
                "Living room",
                SCREENING_DATE
        )).thenReturn(Optional.empty());
        Optional<ScreeningDto> expected = Optional.empty();

        // When
        Optional<ScreeningDto> actual = underTest.getScreeningByMovieTitleAndRoomNameAndStartOfScreening(
                "Spirited Away",
                "Living room",
                SCREENING_DATE
        );

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(screeningRepositoryMock).findByMovieTitleAndRoomNameAndStartOfScreening(
                "Spirited Away",
                "Living room",
                SCREENING_DATE
        );
    }

    @Test
    void testAreScreeningsCollidingShouldReturnTrueWhenTwoExistingScreeningsInTheSameRoomCollide() {
        // Given
        final ScreeningDto DTO2 = new ScreeningDto(
                "Johnny English",
                "Pedersoli",
                LocalDateTime.parse("2022-12-24 15:00",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        when(movieServiceMock.getMovieByTitle(DTO.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Sátántangó",
                        "drama",
                        300
                )));
        when(movieServiceMock.getMovieByTitle(DTO2.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Johnny English",
                        "comedy",
                        150
                ))
        );
        when(roomServiceMock.getRoomByName(DTO.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Pedersoli",
                        20,
                        10
                ))
        );
        when(roomServiceMock.getRoomByName(DTO2.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Pedersoli",
                        20,
                        10
                ))
        );
        boolean expected = true;

        // When
        boolean actual = underTest.areScreeningsColliding(DTO, DTO2);

        // Then
        assertEquals(expected, actual);
        assertTrue(actual);
        verify(movieServiceMock).getMovieByTitle(DTO.getMovieTitle());
        verify(movieServiceMock).getMovieByTitle(DTO2.getMovieTitle());
        verify(roomServiceMock, times(2)).getRoomByName(DTO.getRoomName());
    }

    @Test
    void testAreScreeningsCollidingShouldReturnFalseWhenTwoExistingScreeningsInTheSameRoomDoNotCollide() {
        final ScreeningDto DTO2 = new ScreeningDto(
                "Johnny English",
                "Pedersoli",
                LocalDateTime.parse("2022-12-24 08:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        when(movieServiceMock.getMovieByTitle(DTO.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Sátántangó",
                        "drama",
                        300

                )));
        when(movieServiceMock.getMovieByTitle(DTO2.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Johnny English",
                        "comedy",
                        150
                ))
        );
        when(roomServiceMock.getRoomByName(DTO.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Pedersoli",
                        20,
                        10
                ))
        );
        when(roomServiceMock.getRoomByName(DTO2.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Pedersoli",
                        20,
                        10
                ))
        );
        boolean expected = false;

        // When
        boolean actual = underTest.areScreeningsColliding(DTO, DTO2);

        // Then
        assertEquals(expected, actual);
        assertFalse(actual);
        verify(movieServiceMock).getMovieByTitle(DTO.getMovieTitle());
        verify(movieServiceMock).getMovieByTitle(DTO2.getMovieTitle());
        verify(roomServiceMock, times(2)).getRoomByName(DTO.getRoomName());
    }

    @Test
    void testAreScreeningsCollidingShouldReturnFalseWhenTwoExistingScreeningsAreNotInTheSameRoom() {
        final ScreeningDto DTO2 = new ScreeningDto(
                "Johnny English",
                "Living room",
                LocalDateTime.parse("2022-12-24 08:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        when(movieServiceMock.getMovieByTitle(DTO.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Sátántangó",
                        "drama",
                        300

                )));
        when(movieServiceMock.getMovieByTitle(DTO2.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Johnny English",
                        "comedy",
                        150
                ))
        );
        when(roomServiceMock.getRoomByName(DTO.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Pedersoli",
                        20,
                        10
                ))
        );
        when(roomServiceMock.getRoomByName(DTO2.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Living room",
                        10,
                        10
                ))
        );
        boolean expected = false;

        // When
        boolean actual = underTest.areScreeningsColliding(DTO, DTO2);

        // Then
        assertEquals(expected, actual);
        assertFalse(actual);
        verify(movieServiceMock).getMovieByTitle(DTO.getMovieTitle());
        verify(movieServiceMock).getMovieByTitle(DTO2.getMovieTitle());
        verify(roomServiceMock).getRoomByName(DTO.getRoomName());
        verify(roomServiceMock).getRoomByName(DTO2.getRoomName());
    }

    @Test
    void testAreScreeningsCollidingShouldReturnFalseWhenOneRoomDoesNotExist() {
        final ScreeningDto DTO2 = new ScreeningDto(
                "Johnny English",
                "Living room",
                LocalDateTime.parse("2022-12-24 15:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        when(movieServiceMock.getMovieByTitle(DTO.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Sátántangó",
                        "drama",
                        300
                )));
        when(movieServiceMock.getMovieByTitle(DTO2.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Johnny English",
                        "comedy",
                        150
                ))
        );
        when(roomServiceMock.getRoomByName(DTO.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Pedersoli",
                        20,
                        10
                ))
        );
        when(roomServiceMock.getRoomByName(DTO2.getRoomName())).thenReturn(Optional.empty());
        boolean expected = false;

        // When
        boolean actual = underTest.areScreeningsColliding(DTO, DTO2);

        // Then
        assertEquals(expected, actual);
        assertFalse(actual);
        verify(movieServiceMock).getMovieByTitle(DTO.getMovieTitle());
        verify(movieServiceMock).getMovieByTitle(DTO2.getMovieTitle());
        verify(roomServiceMock).getRoomByName(DTO.getRoomName());
        verify(roomServiceMock).getRoomByName(DTO2.getRoomName());
    }

    @Test
    void testAreScreeningsCollidingShouldReturnFalseWhenOneMovieDoesNotExist() {
        // Given
        final ScreeningDto Dto2 = new ScreeningDto(
                "Kidnapped",
                "Toilet",
                LocalDateTime.parse(
                        "2022-12-24 14:10",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                )
        );
        when(movieServiceMock.getMovieByTitle(DTO.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Sátántangó",
                        "drama",
                        300
                ))
        );
        when(movieServiceMock.getMovieByTitle(Dto2.getMovieTitle())).thenReturn(
                Optional.empty()
        );
        when(roomServiceMock.getRoomByName(DTO.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Pedersoli",
                        20,
                        10
                ))
        );
        when(roomServiceMock.getRoomByName(Dto2.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Toilet",
                        1,
                        1
                ))
        );
        boolean expected = false;

        // When
        boolean actual = underTest.areScreeningsColliding(DTO, Dto2);

        // Then
        assertEquals(expected, actual);
        assertFalse(actual);
        verify(movieServiceMock).getMovieByTitle(DTO.getMovieTitle());
        verify(movieServiceMock).getMovieByTitle(Dto2.getMovieTitle());
        verify(roomServiceMock).getRoomByName(DTO.getRoomName());
        verify(roomServiceMock).getRoomByName(Dto2.getRoomName());
    }

    @Test
    void testIsScreeningCollidingWithTenMinutesBreakPeriodShouldReturnTrueWhenAScreeningInTheSameRoomAsTheOtherOneFallsIntoTheBreakPeriod() {
        final ScreeningDto DTO2 = new ScreeningDto(
                "Johnny English",
                "Pedersoli",
                LocalDateTime.parse("2022-12-24 19:07",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        when(movieServiceMock.getMovieByTitle(DTO.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Sátántangó",
                        "drama",
                        300
                )));
        when(movieServiceMock.getMovieByTitle(DTO2.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Johnny English",
                        "comedy",
                        150
                ))
        );
        when(roomServiceMock.getRoomByName(DTO.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Pedersoli",
                        20,
                        10
                ))
        );
        when(roomServiceMock.getRoomByName(DTO2.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Pedersoli",
                        20,
                        10
                ))
        );
        boolean expected = true;

        // When
        boolean actual = underTest.isScreeningCollidingWithTenMinutesBreakPeriod(DTO, DTO2);

        // Then
        assertEquals(expected, actual);
        assertTrue(actual);
        verify(movieServiceMock).getMovieByTitle(DTO.getMovieTitle());
        verify(movieServiceMock).getMovieByTitle(DTO2.getMovieTitle());
        verify(roomServiceMock, times(2)).getRoomByName(DTO.getRoomName());
    }

    @Test
    void testIsScreeningCollidingWithTenMinutesBreakPeriodShouldReturnFalseWhenAScreeningInTheSameRoomAsTheOtherOneDoesNotFallIntoTheBreakPeriod() {
        final ScreeningDto DTO2 = new ScreeningDto(
                "Johnny English",
                "Pedersoli",
                LocalDateTime.parse("2022-12-24 19:11",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        when(movieServiceMock.getMovieByTitle(DTO.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Sátántangó",
                        "drama",
                        300

                )));
        when(movieServiceMock.getMovieByTitle(DTO2.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Johnny English",
                        "comedy",
                        150
                ))
        );
        when(roomServiceMock.getRoomByName(DTO.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Pedersoli",
                        20,
                        10
                ))
        );
        when(roomServiceMock.getRoomByName(DTO2.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Pedersoli",
                        20,
                        10
                ))
        );
        boolean expected = false;

        // When
        boolean actual = underTest.isScreeningCollidingWithTenMinutesBreakPeriod(DTO, DTO2);

        // Then
        assertEquals(expected, actual);
        assertFalse(actual);
        verify(movieServiceMock).getMovieByTitle(DTO.getMovieTitle());
        verify(movieServiceMock).getMovieByTitle(DTO2.getMovieTitle());
        verify(roomServiceMock, times(2)).getRoomByName(DTO.getRoomName());
    }

    @Test
    void testIsScreeningCollidingWithTenMinutesBreakPeriodShouldReturnFalseWhenAScreeningIsNotInTheSameRoomAsTheOtherOne() {
        final ScreeningDto DTO2 = new ScreeningDto(
                "Johnny English",
                "Kitchen",
                LocalDateTime.parse("2022-12-24 19:03",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        when(movieServiceMock.getMovieByTitle(DTO.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Sátántangó",
                        "drama",
                        300

                )));
        when(movieServiceMock.getMovieByTitle(DTO2.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Johnny English",
                        "comedy",
                        150
                ))
        );
        when(roomServiceMock.getRoomByName(DTO.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Pedersoli",
                        20,
                        10
                ))
        );
        when(roomServiceMock.getRoomByName(DTO2.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Kitchen",
                        4,
                        2
                ))
        );
        boolean expected = false;

        // When
        boolean actual = underTest.isScreeningCollidingWithTenMinutesBreakPeriod(DTO, DTO2);

        // Then
        assertEquals(expected, actual);
        assertFalse(actual);
        verify(movieServiceMock).getMovieByTitle(DTO.getMovieTitle());
        verify(movieServiceMock).getMovieByTitle(DTO2.getMovieTitle());
        verify(roomServiceMock).getRoomByName(DTO.getRoomName());
        verify(roomServiceMock).getRoomByName(DTO2.getRoomName());
    }

    @Test
    void testIsScreeningCollidingWithTenMinutesBreakPeriodShouldReturnFalseWhenOneRoomDoesNotExist() {
        final ScreeningDto DTO2 = new ScreeningDto(
                "Johnny English",
                "Kitchen",
                LocalDateTime.parse("2022-12-24 19:07",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        when(movieServiceMock.getMovieByTitle(DTO.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Sátántangó",
                        "drama",
                        300

                )));
        when(movieServiceMock.getMovieByTitle(DTO2.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Johnny English",
                        "comedy",
                        150
                ))
        );
        when(roomServiceMock.getRoomByName(DTO.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Pedersoli",
                        20,
                        10
                ))
        );
        when(roomServiceMock.getRoomByName(DTO2.getRoomName())).thenReturn(Optional.empty());
        boolean expected = false;

        // When
        boolean actual = underTest.isScreeningCollidingWithTenMinutesBreakPeriod(DTO, DTO2);

        // Then
        assertEquals(expected, actual);
        assertFalse(actual);
        verify(movieServiceMock).getMovieByTitle(DTO.getMovieTitle());
        verify(movieServiceMock).getMovieByTitle(DTO2.getMovieTitle());
        verify(roomServiceMock).getRoomByName(DTO.getRoomName());
        verify(roomServiceMock).getRoomByName(DTO2.getRoomName());
    }
    @Test
    void testIsScreeningCollidingWithTenMinutesBreakPeriodShouldReturnFalseWhenMovieWithBreakAfterDoesNotExist() {
        // Given
        final ScreeningDto Dto2 = new ScreeningDto(
                "Spiderman",
                "Penthouse",
                LocalDateTime.parse(
                        "2022-12-24 15:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                )
        );
        when(movieServiceMock.getMovieByTitle(DTO.getMovieTitle())).thenReturn(
                Optional.empty()
        );
        when(movieServiceMock.getMovieByTitle(Dto2.getMovieTitle())).thenReturn(
                Optional.of(new MovieDto(
                        "Spiderman",
                        "action",
                        140
                ))
        );
        when(roomServiceMock.getRoomByName(DTO.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Pedersoli",
                        20,
                        10
                ))
        );
        when(roomServiceMock.getRoomByName(Dto2.getRoomName())).thenReturn(
                Optional.of(new RoomDto(
                        "Penthouse",
                        200,
                        100
                ))
        );

        boolean expected = false;

        // When
        boolean actual = underTest.isScreeningCollidingWithTenMinutesBreakPeriod(DTO, Dto2);

        // Then
        assertEquals(expected, actual);
        assertFalse(actual);
        verify(movieServiceMock).getMovieByTitle(DTO.getMovieTitle());
        verify(movieServiceMock).getMovieByTitle(Dto2.getMovieTitle());
        verify(roomServiceMock).getRoomByName(DTO.getRoomName());
        verify(roomServiceMock).getRoomByName(Dto2.getRoomName());
    }
}