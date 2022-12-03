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
        return seat.getRowsNumber() > 0
                && seat.getColumnsNumber() > 0
                && seat.getRowsNumber() <= numberOfRows
                && seat.getColumnsNumber() <= numberOfCols;
    }

    @Override
    public String toString() {
        return "Room " + name + " with " + numberOfRows * numberOfCols + " seats, "
                + numberOfRows + " rows and " + numberOfCols + " columns";
    }

}
