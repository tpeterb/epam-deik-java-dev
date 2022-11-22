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
            Optional<Room> roomToUpdate = roomRepository.findById(room.get().getId());
            Room roomValueToUpdate = room.get();
            roomValueToUpdate.setNumberOfRows(roomDto.getNumberOfRows());
            roomValueToUpdate.setNumberOfCols(roomDto.getNumberOfCols());
            roomRepository.save(roomValueToUpdate);
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

    @Override
    public Optional<RoomDto> getRoomByName(String roomName) {
        Optional<Room> room = roomRepository.findByName(roomName);
        if (room.isPresent()) {
            return Optional.of(convertEntityToDto(room.get()));
        }
        return Optional.empty();
    }

    private RoomDto convertEntityToDto(Room room) {
        return new RoomDto(
                room.getName(),
                room.getNumberOfRows(),
                room.getNumberOfCols()
        );
    }

}
