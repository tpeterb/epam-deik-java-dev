package com.epam.training.ticketservice.core.screening.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ScreeningDto {

    private String movieTitle;

    private String roomName;

    private LocalDateTime startOfScreening;

}
