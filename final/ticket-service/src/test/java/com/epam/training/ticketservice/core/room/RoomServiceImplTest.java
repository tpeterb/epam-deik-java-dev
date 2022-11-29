package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepositoryMock;

    @InjectMocks
    private RoomServiceImpl underTest;

    private static final Room ENTITY = new Room(
            "Avalanche",
            30,
            19
    );

    private static final RoomDto DTO = new RoomDto(
            "Avalanche",
            30,
            19
    );

    @Test
    void testCreateRoomShouldStoreTheGivenRoomWhenTheRoomIsValid() {
        // Given
        when(roomRepositoryMock.save(ENTITY)).thenReturn(ENTITY);

        // When
        underTest.createRoom(DTO);

        // Then
        verify(roomRepositoryMock).save(ENTITY);
    }

    @Test
    void testUpdateRoomShouldUpdateTheGivenRoomWhenTheRoomExists() {
        // Given
        final RoomDto dtoForUpdate = new RoomDto(
                "Avalanche",
                20,
                15
        );
        final Room entityToUpdate = new Room(
                1,
                "Avalanche",
                30,
                19
        );
        final Room updatedEntity = new Room(
                1,
                dtoForUpdate.getName(),
                dtoForUpdate.getNumberOfRows(),
                dtoForUpdate.getNumberOfCols()
        );
        when(roomRepositoryMock.findByName(dtoForUpdate.getName())).thenReturn(Optional.of(entityToUpdate));
        when(roomRepositoryMock.findById(entityToUpdate.getId())).thenReturn(Optional.of(entityToUpdate));
        when(roomRepositoryMock.save(updatedEntity)).thenReturn(updatedEntity);

        // When
        underTest.updateRoom(dtoForUpdate);

        // Then
        verify(roomRepositoryMock).findByName(dtoForUpdate.getName());
        verify(roomRepositoryMock).findById(entityToUpdate.getId());
        verify(roomRepositoryMock).save(updatedEntity);
    }

    @Test
    void testUpdateRoomShouldNotUpdateAnythingWhenTheGivenRoomDoesNotExist() {
        // Given
        final RoomDto dtoForUpdate = new RoomDto("Julius Caesar", 10, 10);
        when(roomRepositoryMock.findByName(dtoForUpdate.getName())).thenReturn(Optional.empty());

        // When
        underTest.updateRoom(dtoForUpdate);

        // Then
        verify(roomRepositoryMock).findByName(dtoForUpdate.getName());
        verifyNoMoreInteractions(roomRepositoryMock);
    }

    @Test
    void testDeleteRoomShouldDeleteTheGivenRoomWhenTheRoomExists() {
        // Given
        when(roomRepositoryMock.findByName(DTO.getName())).thenReturn(Optional.of(ENTITY));

        // When
        underTest.deleteRoom(DTO.getName());

        // Then
        verify(roomRepositoryMock).findByName(DTO.getName());
        verify(roomRepositoryMock).delete(ENTITY);
    }

    @Test
    void testDeleteRoomShouldNotDeleteAnythingWhenTheGivenRoomDoesNotExist() {
        // Given
        when(roomRepositoryMock.findByName("July")).thenReturn(Optional.empty());

        // When
        underTest.deleteRoom("July");

        // Then
        verify(roomRepositoryMock).findByName("July");
        verifyNoMoreInteractions(roomRepositoryMock);
    }

    @Test
    void testGetRoomListShouldReturnAListWithOneElement() {
        // Given
        when(roomRepositoryMock.findAll()).thenReturn(List.of(ENTITY));
        List<RoomDto> expected = List.of(DTO);

        // When
        List<RoomDto> actual = underTest.getRoomList();

        // Then
        assertEquals(expected, actual);
        assertFalse(actual.isEmpty());
        verify(roomRepositoryMock).findAll();
    }

    @Test
    void testGetRoomByNameShouldReturnANonEmptyOptionalWhenTheGivenRoomExists() {
        // Given
        when(roomRepositoryMock.findByName("Avalanche")).thenReturn(Optional.of(ENTITY));
        Optional<RoomDto> expected = Optional.of(DTO);

        // When
        Optional<RoomDto> actual = underTest.getRoomByName("Avalanche");

        // Then
        assertEquals(expected, actual);
        assertFalse(actual.isEmpty());
        verify(roomRepositoryMock).findByName("Avalanche");
    }

    @Test
    void testGetRoomByNameShouldReturnAnEmptyOptionalWhenTheGivenRoomDoesNotExist() {
        // Given
        when(roomRepositoryMock.findByName("Pillow")).thenReturn(Optional.empty());
        Optional<RoomDto> expected = Optional.empty();

        // When
        Optional<RoomDto> actual = underTest.getRoomByName("Pillow");

        // Then
        assertEquals(expected, actual);
        assertTrue(actual.isEmpty());
        verify(roomRepositoryMock).findByName("Pillow");
    }

}