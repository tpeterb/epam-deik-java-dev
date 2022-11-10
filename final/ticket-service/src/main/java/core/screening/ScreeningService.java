package core.screening;

import core.screening.model.ScreeningDto;
import core.screening.persistence.entity.Screening;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningService {

    void createScreening(ScreeningDto screeningDto);

    void deleteScreening(ScreeningDto screeningDto);

    List<ScreeningDto> getScreeningList();

}
