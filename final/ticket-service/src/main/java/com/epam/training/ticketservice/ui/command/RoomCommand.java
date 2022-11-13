package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class RoomCommand {

    private final RoomService roomService;

    private final UserService userService;

    @ShellMethod(key = "create room")
    @ShellMethodAvailability("isAvailable")
    public RoomDto createRoom(String name, int numberOfRows, int numberOfCols) {
        RoomDto roomDto = new RoomDto(name, numberOfRows, numberOfCols);
        roomService.createRoom(roomDto);
        return roomDto;
    }

    @ShellMethod(key = "update room")
    @ShellMethodAvailability("isAvailable")
    public RoomDto updateRoom(String name, int numberOfRows, int numberOfCols) {
        RoomDto roomDto = new RoomDto(name, numberOfRows, numberOfCols);
        roomService.updateRoom(roomDto);
        return roomDto;
    }

    @ShellMethod(key = "delete room")
    @ShellMethodAvailability("isAvailable")
    public void deleteRoom(String name) {
        roomService.deleteRoom(name);
    }

    @ShellMethod(key = "list rooms")
    public String listRooms() {
        List<RoomDto> rooms = roomService.getRoomList();
        if (rooms.isEmpty()) {
            return "There are no rooms at the moment";
        }
        return rooms.toString();
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }

}
