package core.room;

import core.room.model.RoomDto;
import core.room.persistence.entity.Room;

import java.util.List;

public interface RoomService {

    void createRoom(RoomDto roomDto);

    void updateRoom(RoomDto roomDto);

    void deleteRoom(String name);

    List<RoomDto> getRoomList();

}
