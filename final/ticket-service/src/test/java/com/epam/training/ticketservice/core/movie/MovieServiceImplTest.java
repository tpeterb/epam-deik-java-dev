package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceImplTest {

    private final MovieRepository movieRepositoryMock = mock(MovieRepository.class);

    private final MovieService underTest = new MovieServiceImpl(movieRepositoryMock);

    private static final Movie ENTITY = new Movie(
            "Johnny English",
            "comedy",
            123
    );

    private static final MovieDto DTO = new MovieDto(
            "Johnny English",
            "comedy",
            123
    );

    @Test
    void testCreateMovieShouldStoreTheGivenMovieWhenTheInputMovieIsValid() {
        // Given
        when(movieRepositoryMock.save(ENTITY)).thenReturn(ENTITY);

        // When
        underTest.createMovie(DTO);

        // Then
        verify(movieRepositoryMock).save(ENTITY);
    }

    @Test
    void testUpdateMovieShouldUpdateMovieWhenTheGivenMovieExists() {
        // Given
        final Movie movieToUpdate = new Movie(
                1,
                "Johnny English",
                "comedy",
                123
        );
        final MovieDto dtoForUpdate = new MovieDto(
                "Johnny English",
                "thriller",
                169
        );
        final Movie updatedMovie = new Movie(
                1,
                dtoForUpdate.getTitle(),
                dtoForUpdate.getGenre(),
                dtoForUpdate.getLengthInMinutes()
        );
        when(movieRepositoryMock.findByTitle(dtoForUpdate.getTitle())).thenReturn(
                Optional.of(movieToUpdate)
        );
        when(movieRepositoryMock.findById(movieToUpdate.getId())).thenReturn(
                Optional.of(movieToUpdate)
        );
        when(movieRepositoryMock.save(updatedMovie)).thenReturn(updatedMovie);

        // When
        underTest.updateMovie(dtoForUpdate);

        // Then
        verify(movieRepositoryMock).save(updatedMovie);
    }

    @Test
    void testUpdateMovieShouldNotUpdateAnythingWhenTheGivenMovieDoesNotExist() {
        // Given
        final MovieDto dtoForUpdate = new MovieDto(
                "The Betrayal",
                "action",
                100
        );
        when(movieRepositoryMock.findByTitle(dtoForUpdate.getTitle())).thenReturn(Optional.empty());

        // When
        underTest.updateMovie(dtoForUpdate);

        // Then
        verify(movieRepositoryMock).findByTitle(dtoForUpdate.getTitle());
        verifyNoMoreInteractions(movieRepositoryMock);
    }

    @Test
    void testDeleteMovieShouldDeleteTheGivenMovieWhenTheMovieExists() {
        // Given
        when(movieRepositoryMock.findByTitle("Johnny English")).thenReturn(Optional.of(ENTITY));

        // When
        underTest.deleteMovie("Johnny English");

        // Then
        verify(movieRepositoryMock).delete(ENTITY);
    }

    @Test
    void testDeleteMovieShouldNotDeleteAnythingWhenTheGivenMovieDoesNotExist() {
        // Given
        when(movieRepositoryMock.findByTitle("Calculator")).thenReturn(Optional.empty());

        // When
        underTest.deleteMovie("Calculator");

        // Then
        verify(movieRepositoryMock).findByTitle("Calculator");
        verifyNoMoreInteractions(movieRepositoryMock);
    }

    @Test
    void testGetMovieListShouldReturnAListWithOneElement() {
        // Given
        when(movieRepositoryMock.findAll()).thenReturn(List.of(ENTITY));
        List<MovieDto> expected = List.of(DTO);

        // When
        List<MovieDto> actual = underTest.getMovieList();

        // Then
        assertEquals(expected, actual);
        assertFalse(actual.isEmpty());
        verify(movieRepositoryMock).findAll();
    }

    @Test
    void testGetMovieByTitleShouldReturnANonEmptyOptionalWhenTheGivenMovieExists() {
        // Given
        when(movieRepositoryMock.findByTitle("Johnny English")).thenReturn(Optional.of(ENTITY));
        Optional<MovieDto> expected = Optional.of(DTO);

        // When
        Optional<MovieDto> actual = underTest.getMovieByTitle("Johnny English");

        // Then
        assertEquals(expected, actual);
        assertFalse(actual.isEmpty());
        verify(movieRepositoryMock).findByTitle("Johnny English");
    }

    @Test
    void testGetMovieByTitleShouldReturnAnEmptyOptionalWhenTheGivenMovieDoesNotExist() {
        // Given
        when(movieRepositoryMock.findByTitle("Driver")).thenReturn(Optional.empty());
        Optional<MovieDto> expected = Optional.empty();

        // When
        Optional<MovieDto> actual = underTest.getMovieByTitle("Driver");

        // Then
        assertEquals(expected, actual);
        assertTrue(actual.isEmpty());
        verify(movieRepositoryMock).findByTitle("Driver");
    }

}