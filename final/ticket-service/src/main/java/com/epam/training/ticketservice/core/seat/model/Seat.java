package com.epam.training.ticketservice.core.seat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Objects;

@Entity
@Table(name = "szek")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer rowNumber;

    private Integer columnNumber;

    public Seat(Integer rowNumber, Integer columnNumber) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
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
        return this.rowNumber == other.getRowNumber()
                && this.columnNumber == other.columnNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNumber, columnNumber);
    }

    @Override
    public String toString() {
        return "(" + rowNumber + "," + columnNumber + ")";
    }

}
