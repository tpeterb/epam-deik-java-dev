package core.screening.persistence.entity;

import core.movie.persistence.entity.Movie;
import core.room.persistence.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "vetites")
public class Screening {

    @EmbeddedId
    private ScreeningId screeningId;

}
