package com.epam.training.ticketservice.core.room.persistence.entity;

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
@Table(name = "terem")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String name;

    private Integer numberOfRows;

    private Integer numberOfCols;

    public Room(String name, Integer numberOfRows, Integer numberOfCols) {
        this.name = name;
        this.numberOfRows = numberOfRows;
        this.numberOfCols = numberOfCols;
    }

}
