package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public void createRoom(RoomDto roomDto) {
        Room room = new Room(
                roomDto.getName(),
                roomDto.getNumberOfRows(),
                roomDto.getNumberOfCols()
        );
        roomRepository.save(room);
    }

    @Override
    public void updateRoom(RoomDto roomDto) {
        Optional<Room> room = roomRepository.findByName(roomDto.getName());
        if (room.isPresent()) {
            Room roomValue = room.get();
            roomValue.setNumberOfRows(roomDto.getNumberOfRows());
            roomValue.setNumberOfCols(roomDto.getNumberOfCols());
        }
    }

    @Override
    public void deleteRoom(String name) {
        Optional<Room> room = roomRepository.findByName(name);
        if (room.isPresent()) {
            roomRepository.delete(room.get());
        }
    }

    @Override
    public List<RoomDto> getRoomList() {
        return roomRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private RoomDto convertEntityToDto(Room room) {
        return new RoomDto(
                room.getName(),
                room.getNumberOfRows(),
                room.getNumberOfCols()
        );
    }

}
