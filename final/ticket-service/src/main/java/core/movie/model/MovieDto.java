package core.movie.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MovieDto {

    private String title;

    private String genre;

    private int lengthInMinutes;

}
