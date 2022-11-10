package ui.command;

import core.room.RoomService;
import core.room.model.RoomDto;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class RoomCommand {

    private final RoomService roomService;

    @ShellMethod(key = "create room")
    public RoomDto createRoom(String name, int numberOfRows, int numberOfCols) {
        RoomDto roomDto = new RoomDto(name, numberOfRows, numberOfCols);
        roomService.createRoom(roomDto);
        return roomDto;
    }

    @ShellMethod(key = "update room")
    public RoomDto updateRoom(String name, int numberOfRows, int numberOfCols) {
        RoomDto roomDto = new RoomDto(name, numberOfRows, numberOfCols);
        roomService.updateRoom(roomDto);
        return roomDto;
    }

    @ShellMethod(key = "delete room")
    public void deleteRoom(String name) {
            
    }

}
