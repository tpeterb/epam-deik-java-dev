package com.epam.training.ticketservice.core.movie.persistence.entity;

import com.epam.training.ticketservice.core.finance.pricecomponent.persistence.entity.PriceComponent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "film")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String title;

    private String genre;

    private Integer lengthInMinutes;

    public Movie(String title, String genre, Integer lengthInMinutes) {
        this.title = title;
        this.genre = genre;
        this.lengthInMinutes = lengthInMinutes;
    }

}
