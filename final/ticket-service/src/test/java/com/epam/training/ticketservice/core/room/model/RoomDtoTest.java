package com.epam.training.ticketservice.core.room.model;

import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.seat.model.Seat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoomDtoTest {

    private static final RoomDto ROOM = new RoomDto(
            "Kitchen",
            18,
            20
    );

    private static Stream<Arguments> provideSeatsForDoesSeatExistInRoom() {
        return Stream.of(
                Arguments.of(new Seat(1,1), true),
                Arguments.of(new Seat(10, 10), true),
                Arguments.of(new Seat(0, 4), false),
                Arguments.of(new Seat(7, -13), false),
                Arguments.of(new Seat(-1, 0), false),
                Arguments.of(new Seat(5, 2), true),
                Arguments.of(new Seat(13, 16), true),
                Arguments.of(new Seat(100, 136), false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideSeatsForDoesSeatExistInRoom")
    void testDoesSeatExistInRoom(Seat input, boolean expected) {
        assertEquals(expected, ROOM.doesSeatExistInRoom(input));
    }

}