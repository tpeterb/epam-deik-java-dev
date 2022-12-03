package com.epam.training.ticketservice.core.seat.model;

import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import java.util.Objects;

@Entity
@Table(name = "szekek")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seat_id")
    private Integer id;

    private Integer rowsNumber;

    private Integer columnsNumber;

    public Seat(Integer rowNumber, Integer columnNumber) {
        this.rowsNumber = rowNumber;
        this.columnsNumber = columnNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seat)) {
            return false;
        }
        Seat other = (Seat)o;
        return this.rowsNumber == other.getRowsNumber()
                && this.columnsNumber == other.getColumnsNumber();
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowsNumber, columnsNumber);
    }

    @Override
    public String toString() {
        return "(" + rowsNumber + "," + columnsNumber + ")";
    }

}
