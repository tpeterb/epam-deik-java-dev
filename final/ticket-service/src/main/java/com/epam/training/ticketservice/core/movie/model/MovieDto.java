package com.epam.training.ticketservice.core.movie.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MovieDto {

    private String title;

    private String genre;

    private int lengthInMinutes;

    @Override
    public String toString() {
        return title + " (" + genre + ", " + lengthInMinutes + " minutes)";
    }

}
