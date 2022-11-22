package com.epam.training.ticketservice.core.room.model;

import com.epam.training.ticketservice.core.seat.model.Seat;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RoomDto {

    private String name;

    private Integer numberOfRows;

    private Integer numberOfCols;

    public boolean doesSeatExistInRoom(Seat seat) {
        return seat.getRowNumber() > 0
                && seat.getColumnNumber() > 0
                && seat.getRowNumber() <= numberOfRows
                && seat.getColumnNumber() <= numberOfCols;
    }

    @Override
    public String toString() {
        return "Room " + name + " with " + numberOfRows * numberOfCols + " seats, "
                + numberOfRows + " rows and " + numberOfCols + " columns";
    }

}
