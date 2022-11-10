package core.room.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RoomDto {

    private String name;

    private Integer numberOfRows;

    private Integer numberOfCols;

}
