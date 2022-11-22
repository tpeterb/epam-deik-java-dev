package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ScreeningDto {

    private String movieTitle;

    private String roomName;

    private LocalDateTime startOfScreening;

}
