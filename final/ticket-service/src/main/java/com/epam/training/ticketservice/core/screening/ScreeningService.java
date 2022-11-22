package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningService {

    String createScreening(ScreeningDto screeningDto);

    void deleteScreening(ScreeningDto screeningDto);

    List<ScreeningDto> getScreeningList();

    List<ScreeningDto> getScreeningListByRoom(String roomName);

    Optional<ScreeningDto> getScreeningByMovieTitleAndRoomNameAndStartOfScreening(
            String movieTitle,
            String roomName,
            LocalDateTime startOfScreening);

    boolean areScreeningsColliding(ScreeningDto screeningDto1,
                                   ScreeningDto screeningDto2);

    boolean isScreeningCollidingWithTenMinutesBreakPeriod(
            ScreeningDto screeningDtoWithBreakAfter,
            ScreeningDto screeningDtoWhichMightFallInBreakPeriod);

}
