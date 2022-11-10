package core.room.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "terem")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String name;

    private Integer numberOfRows;

    private Integer numberOfCols;

}
