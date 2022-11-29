package com.epam.training.ticketservice.core.finance.pricecomponent;

import com.epam.training.ticketservice.core.finance.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.core.finance.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.finance.pricecomponent.persistence.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceComponentServiceImplTest {

    @Mock
    private PriceComponentRepository priceComponentRepositoryMock;

    @Mock
    private RoomRepository roomRepositoryMock;

    @Mock
    private MovieRepository movieRepositoryMock;

    @Mock
    private ScreeningRepository screeningRepositoryMock;

    @InjectMocks
    private PriceComponentServiceImpl underTest;

    private static final PriceComponent ENTITY = new PriceComponent(
            "PriceComponent1",
            500
    );

    private static final PriceComponentDto DTO = new PriceComponentDto(
            "PriceComponent1",
            500
    );

    @Test
    void testCreatePriceComponentShouldStoreTheGivenPriceComponentWhenTheInputDataIsValid() {
        // Given
        when(priceComponentRepositoryMock.save(ENTITY)).thenReturn(ENTITY);

        // When
        underTest.createPriceComponent(DTO);

        // Then
        verify(priceComponentRepositoryMock).save(ENTITY);
    }

    @Test
    void testGetPriceComponentByNameShouldReturnANonEmptyOptionalWhenTheGivenPriceComponentExists() {
        // Given
        when(priceComponentRepositoryMock.findByName("PriceComponent1")).thenReturn(
                Optional.of(ENTITY)
        );
        Optional<PriceComponentDto> expected = Optional.of(DTO);

        // When
        Optional<PriceComponentDto> actual = underTest.getPriceComponentByName("PriceComponent1");

        // Then
        assertEquals(expected, actual);
        assertFalse(actual.isEmpty());
        verify(priceComponentRepositoryMock).findByName("PriceComponent1");
    }

    @Test
    void testGetPriceComponentByNameShouldReturnAnEmptyOptionalWhenTheGivenPriceComponentDoesNotExist() {
        // Given
        when(priceComponentRepositoryMock.findByName("dummy")).thenReturn(Optional.empty());
        Optional<PriceComponentDto> expected = Optional.empty();

        // When
        Optional<PriceComponentDto> actual = underTest.getPriceComponentByName("dummy");

        // Then
        assertEquals(expected, actual);
        assertTrue(actual.isEmpty());
        verify(priceComponentRepositoryMock).findByName("dummy");
    }

    @Test
    void testGetBasePriceComponentShouldReturnANonEmptyOptionalWhenTheBasePriceExists() {
        // Given
        when(priceComponentRepositoryMock.findByName("BasePriceComponent")).thenReturn(
                Optional.of(new PriceComponent("BasePriceComponent", 1400))
        );
        Optional<PriceComponentDto> expected = Optional.of(
                new PriceComponentDto("BasePriceComponent", 1400)
        );

        // When
        Optional<PriceComponentDto> actual = underTest.getBasePriceComponent();

        // Then
        assertEquals(expected, actual);
        assertFalse(actual.isEmpty());
        verify(priceComponentRepositoryMock).findByName("BasePriceComponent");
    }

    @Test
    void testGetBasePriceComponentShouldReturnAnEmptyOptionalWhenTheBasePriceDoesNotExist() {
        // Given
        when(priceComponentRepositoryMock.findByName("BasePriceComponent")).thenReturn(Optional.empty());
        Optional<PriceComponentDto> expected = Optional.empty();

        // When
        Optional<PriceComponentDto> actual = underTest.getBasePriceComponent();

        // Then
        assertEquals(expected, actual);
        assertTrue(actual.isEmpty());
        verify(priceComponentRepositoryMock).findByName("BasePriceComponent");
    }

    @Test
    void testGetPriceComponentsByMovieShouldReturnAListWithTwoElements() {
        // Given
        when(priceComponentRepositoryMock.findAll()).thenReturn(List.of(
                new PriceComponent(
                        4,
                        "PriceComponent4",
                        1020,
                        Set.of(),
                        Set.of(
                                new Movie("Kidnapped 2", "action", 142)
                        ),
                        Set.of()),
                new PriceComponent(
                        2,
                        "PriceComponent2",
                        400,
                        Set.of(),
                        Set.of(
                                new Movie("Johnny English", "comedy", 140),
                                new Movie("The Betrayal", "comedy", 100)
                        ),
                        Set.of()),
                new PriceComponent(
                        3,
                        "PriceComponent3",
                        545,
                        Set.of(),
                        Set.of(
                                new Movie("Johnny English", "comedy", 140),
                                new Movie("The Calculator", "action", 125)
                        ),
                        Set.of())
        ));
        List<PriceComponentDto> expected = List.of(
                new PriceComponentDto("PriceComponent2", 400),
                new PriceComponentDto("PriceComponent3", 545)
        );

        // When
        List<PriceComponentDto> actual = underTest.getPriceComponentsByMovie("Johnny English");

        // Then
        assertEquals(expected, actual);
        assertFalse(actual.isEmpty());
        assertEquals(2, actual.size());
        verify(priceComponentRepositoryMock).findAll();
    }

    @Test
    void testGetPriceComponentsByRoomShouldReturnAListWithOneElement() {
        // Given
        when(priceComponentRepositoryMock.findAll()).thenReturn(List.of(
                new PriceComponent(
                        2,
                        "PriceComponent2",
                       875,
                       Set.of(
                               new Room("Kitchen", 20, 14)
                       ),
                       Set.of(
                               new Movie("Johnny English", "comedy", 140),
                               new Movie("The Calculator", "action", 125)
                       ),
                       Set.of()
                ),
                new PriceComponent(
                        3,
                        "PriceComponent3",
                        1000,
                        Set.of(
                                new Room("Living room", 10, 25),
                                new Room("Bedroom", 14, 17)
                        ),
                        Set.of(
                                new Movie("Kidnapped 2", "action", 142)
                        ),
                        Set.of()
                )
        ));
        List<PriceComponentDto> expected = List.of(
                new PriceComponentDto("PriceComponent2", 875)
        );

        // When
        List<PriceComponentDto> actual = underTest.getPriceComponentsByRoom("Kitchen");

        // Then
        assertEquals(expected, actual);
        assertFalse(actual.isEmpty());
        assertEquals(1, actual.size());
        verify(priceComponentRepositoryMock).findAll();
    }

    @Test
    void testGetPriceComponentsByScreeningShouldReturnAListWithOneElement() {
        // Given
        final ScreeningDto screeningDto = new ScreeningDto(
                "The Calculator",
                "Bedroom",
                LocalDateTime.parse(
                        "2022-04-18 17:25",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        ));
        when(priceComponentRepositoryMock.findAll()).thenReturn(List.of(
                new PriceComponent(
                        2,
                        "PriceComponent2",
                        1500,
                        Set.of(
                                new Room("Kitchen", 20, 14)
                        ),
                        Set.of(
                                new Movie("Kidnapped 2", "action", 142)
                        ),
                        Set.of(
                                new Screening(
                                        "Kidnapped 2",
                                        "Toilet",
                                        LocalDateTime.parse(
                                                "2023-04-05 10:00",
                                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                        )
                                ),
                                new Screening(
                                        "The Horseshoe Remover",
                                        "Bedroom",
                                        LocalDateTime.parse(
                                                "2022-04-18 17:25",
                                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                        )
                                )
                        )
                ),
                new PriceComponent(
                        3,
                        "PriceComponent3",
                        560,
                        Set.of(
                                new Room("Living room", 10, 25),
                                new Room("Bedroom", 14, 17)
                        ),
                        Set.of(
                                new Movie("Johnny English", "comedy", 140),
                                new Movie("The Calculator", "action", 125)
                        ),
                        Set.of(
                                new Screening(
                                      "The Calculator",
                                      "Bedroom",
                                      LocalDateTime.parse(
                                              "2022-04-18 17:25",
                                              DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                      )
                                ),
                                new Screening(
                                        "Johnny English",
                                        "Kitchen",
                                        LocalDateTime.parse(
                                                "2023-01-02 15:30",
                                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                        )
                                )
                        )
                )
        ));
        List<PriceComponentDto> expected = List.of(
                new PriceComponentDto("PriceComponent3", 560)
        );

        // When
        List<PriceComponentDto> actual = underTest.getPriceComponentsByScreening(screeningDto);

        // Then
        assertEquals(expected, actual);
        assertFalse(actual.isEmpty());
        assertEquals(1, actual.size());
        verify(priceComponentRepositoryMock).findAll();
    }

    @Test
    void testUpdateBasePriceComponentShouldUpdateAlreadyExistingBasePriceComponentWithPositiveInteger() {
        int newBasePriceAmount = 2000;
        final PriceComponent basePriceComponent = new PriceComponent(
                    1,
                    "BasePriceComponent",
                    1650,
                    Set.of(),
                    Set.of(),
                    Set.of()
        );
        final PriceComponent basePriceComponentUpdated = new PriceComponent(
                1,
                "BasePriceComponent",
                newBasePriceAmount,
                Set.of(),
                Set.of(),
                Set.of()
        );
        when(priceComponentRepositoryMock.findByName("BasePriceComponent")).thenReturn(
                Optional.of(basePriceComponent)
        );
        when(priceComponentRepositoryMock.findById(1)).thenReturn(Optional.of(basePriceComponent));
        when(priceComponentRepositoryMock.save(basePriceComponentUpdated)).thenReturn(basePriceComponentUpdated);

        // When
        underTest.updateBasePriceComponent(newBasePriceAmount);

        // Then
        verify(priceComponentRepositoryMock).save(basePriceComponentUpdated);
    }

    @Test
    void testUpdateBasePriceComponentShouldNotUpdateAlreadyExistingBasePriceComponentWithNegativeInteger() {
        // Given
        int newBasePriceAmount = -1250;
        PriceComponent basePriceComponent = new PriceComponent(
                1,
                "BasePriceComponent",
                1500,
                Set.of(),
                Set.of(),
                Set.of()
        );
        when(priceComponentRepositoryMock.findByName("BasePriceComponent")).thenReturn(
                Optional.of(basePriceComponent)
        );
        when(priceComponentRepositoryMock.findById(1)).thenReturn(
                Optional.of(basePriceComponent)
        );

        // When
        underTest.updateBasePriceComponent(newBasePriceAmount);

        // Then
        verify(priceComponentRepositoryMock).findByName("BasePriceComponent");
        verify(priceComponentRepositoryMock).findById(1);
        verifyNoMoreInteractions(priceComponentRepositoryMock);
    }

    @Test
    void testUpdateBasePriceComponentShouldNotUpdateAnythingWhenTheBasePriceDoesNotExist() {
        // Given
        int newBasePriceAmount = 1000;
        when(priceComponentRepositoryMock.findByName("BasePriceComponent")).
                thenReturn(Optional.empty());

        // When
        underTest.updateBasePriceComponent(newBasePriceAmount);

        // Then
        verify(priceComponentRepositoryMock).findByName("BasePriceComponent");
        verifyNoMoreInteractions(priceComponentRepositoryMock);
    }

    @Test
    void testAddPriceComponentToRoomShouldAddPriceComponentToRoomWhenBothExist() {
        // Given
        final Room roomToUse = new Room(
                "Kitchen",
                20,
                11
        );
        PriceComponent priceComponentToAddToRoom = new PriceComponent(
                1,
                "PriceComponentToAdd",
                400,
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>()
        );
        PriceComponentDto priceComponentDto = new PriceComponentDto(
                priceComponentToAddToRoom.getName(),
                priceComponentToAddToRoom.getAmount()
        );
        when(priceComponentRepositoryMock.findByName("PriceComponentToAdd")).thenReturn(Optional.of(priceComponentToAddToRoom));
        when(roomRepositoryMock.findByName(roomToUse.getName())).thenReturn(Optional.of(roomToUse));
        Set<Room> expectedRooms = Set.of(roomToUse);

        // When
        underTest.addPriceComponentToRoom(priceComponentDto, roomToUse.getName());

        // Then
        verify(priceComponentRepositoryMock).findByName("PriceComponentToAdd");
        verify(roomRepositoryMock).findByName(roomToUse.getName());
        assertEquals(expectedRooms, priceComponentToAddToRoom.getRooms());
        assertFalse(priceComponentToAddToRoom.getRooms().isEmpty());
    }
    @Test
    void testAddPriceComponentToRoomShouldNotAddPriceComponentToRoomWhenEitherTheRoomOrThePriceComponentDoesNotExist() {
        // Given
        PriceComponent priceComponentToAddToRoom = new PriceComponent(
                10,
                "PriceComponentToAdd",
                5000,
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>()
        );
        PriceComponentDto priceComponentDto = new PriceComponentDto(
                priceComponentToAddToRoom.getName(),
                priceComponentToAddToRoom.getAmount()
        );
        when(priceComponentRepositoryMock.findByName(priceComponentToAddToRoom.getName())).thenReturn(
                Optional.of(priceComponentToAddToRoom)
        );
        when(roomRepositoryMock.findByName("Toilet")).thenReturn(Optional.empty());

        Set<Room> expectedRooms = Set.of();

        // When
        underTest.addPriceComponentToRoom(priceComponentDto, "Toilet");

        // Then
        assertEquals(expectedRooms, priceComponentToAddToRoom.getRooms());
        assertTrue(priceComponentToAddToRoom.getRooms().isEmpty());
        verify(priceComponentRepositoryMock).findByName("PriceComponentToAdd");
        verify(roomRepositoryMock).findByName("Toilet");
    }

    @Test
    void testAddPriceComponentToMovieShouldAddPriceComponentToMovieWhenBothExist() {
        // Given
        PriceComponent priceComponentToAdd = new PriceComponent(
                15,
                "Some tax",
                900,
                new HashSet<Room>(),
                new HashSet<Movie>(),
                new HashSet<Screening>()
        );
        PriceComponentDto dto = new PriceComponentDto(
                priceComponentToAdd.getName(),
                priceComponentToAdd.getAmount()
        );
        final Movie movie = new Movie(
                "Johnny English",
                "comedy",
                300
        );
        when(priceComponentRepositoryMock.findByName("Some tax")).thenReturn(Optional.of(priceComponentToAdd));
        when(movieRepositoryMock.findByTitle("Johnny English")).thenReturn(Optional.of(movie));

        Set<Movie> expectedMovies = Set.of(movie);

        // When
        underTest.addPriceComponentToMovie(dto, "Johnny English");

        // Then
        assertEquals(expectedMovies, priceComponentToAdd.getMovies());
        assertFalse(priceComponentToAdd.getMovies().isEmpty());
        verify(priceComponentRepositoryMock).findByName("Some tax");
        verify(movieRepositoryMock).findByTitle("Johnny English");
    }

    @Test
    void testAddPriceComponentToMovieShouldNotAddPriceComponentToMovieWhenOneOfThemDoesNotExist() {
        // Given
        PriceComponent priceComponentToAdd = new PriceComponent(
                23,
                "Screening Fee",
                200,
                new HashSet<Room>(),
                new HashSet<Movie>(),
                new HashSet<Screening>()
        );
        PriceComponentDto dto = new PriceComponentDto(
                priceComponentToAdd.getName(),
                priceComponentToAdd.getAmount()
        );
        when(priceComponentRepositoryMock.findByName("Screening Fee")).thenReturn(Optional.of(priceComponentToAdd));
        when(movieRepositoryMock.findByTitle("Spring Shell")).thenReturn(Optional.empty());

        Set<Movie> expectedMovies = Set.of();

        // When
        underTest.addPriceComponentToMovie(dto, "Spring Shell");

        // Then
        assertEquals(expectedMovies, priceComponentToAdd.getMovies());
        assertTrue(priceComponentToAdd.getMovies().isEmpty());
        verify(priceComponentRepositoryMock).findByName("Screening Fee");
        verify(movieRepositoryMock).findByTitle("Spring Shell");
    }

    @Test
    void testAddPriceComponentToScreeningShouldAddPriceComponentToScreeningWhenBothExist() {
        // Given
        PriceComponent priceComponentToAdd = new PriceComponent(
                15,
                "Movie tax",
                650,
                new HashSet<Room>(),
                new HashSet<Movie>(),
                new HashSet<Screening>()
        );
        PriceComponentDto dto = new PriceComponentDto(
                priceComponentToAdd.getName(),
                priceComponentToAdd.getAmount()
        );
        final Screening screening = new Screening(
                "Johnny English",
                "Toilet",
                LocalDateTime.parse(
                        "2022-12-20 14:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                )
        );
        when(priceComponentRepositoryMock.findByName("Movie tax")).thenReturn(
                Optional.of(priceComponentToAdd)
        );
        when(screeningRepositoryMock.findByMovieTitleAndRoomNameAndStartOfScreening(
                screening.getMovieTitle(),
                screening.getRoomName(),
                screening.getStartOfScreening()
        )).thenReturn(Optional.of(screening));

        Set<Screening> expectedScreenings = Set.of(screening);

        // When
        underTest.addPriceComponentToScreening(dto, new ScreeningDto(
                screening.getMovieTitle(),
                screening.getRoomName(),
                screening.getStartOfScreening()
        ));

        // Then
        assertEquals(expectedScreenings, priceComponentToAdd.getScreenings());
        assertFalse(priceComponentToAdd.getScreenings().isEmpty());
        verify(priceComponentRepositoryMock).findByName("Movie tax");
        verify(screeningRepositoryMock).findByMovieTitleAndRoomNameAndStartOfScreening(
                screening.getMovieTitle(),
                screening.getRoomName(),
                screening.getStartOfScreening()
        );
    }

    @Test
    void testAddPriceComponentToScreeningShouldNotAddPriceComponentToScreeningWhenOneOfThemDoesNotExist() {
        // Given
        PriceComponent priceComponentToAdd = new PriceComponent(
                25,
                "PriceComponent",
                1500,
                new HashSet<Room>(),
                new HashSet<Movie>(),
                new HashSet<Screening>()
        );
        PriceComponentDto priceComponentDto = new PriceComponentDto(
                priceComponentToAdd.getName(),
                priceComponentToAdd.getAmount()
        );
        final ScreeningDto screeningDto = new ScreeningDto(
                "Rudolph the red nosed reindeer",
                "Santa's House",
                LocalDateTime.parse(
                        "2022-12-20 14:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                )
        );
        when(priceComponentRepositoryMock.findByName("PriceComponent")).thenReturn(
                Optional.of(priceComponentToAdd)
        );
        when(screeningRepositoryMock.findByMovieTitleAndRoomNameAndStartOfScreening(
                screeningDto.getMovieTitle(),
                screeningDto.getRoomName(),
                screeningDto.getStartOfScreening()
        )).thenReturn(Optional.empty());

        Set<Screening> expectedScreenings = Set.of();

        // When
        underTest.addPriceComponentToScreening(priceComponentDto, screeningDto);

        // Then
        assertEquals(expectedScreenings, priceComponentToAdd.getScreenings());
        assertTrue(priceComponentToAdd.getScreenings().isEmpty());
        verify(priceComponentRepositoryMock).findByName("PriceComponent");
        verify(screeningRepositoryMock).findByMovieTitleAndRoomNameAndStartOfScreening(
                screeningDto.getMovieTitle(),
                screeningDto.getRoomName(),
                screeningDto.getStartOfScreening()
        );
    }

}