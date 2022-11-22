package com.epam.training.ticketservice.core.screening.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "vetites", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "movieTitle", "roomName", "startOfScreening" })})
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String movieTitle;

    private String roomName;

    private LocalDateTime startOfScreening;

    public Screening(String movieTitle, String roomName, LocalDateTime startOfScreening) {
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.startOfScreening = startOfScreening;
    }

}
