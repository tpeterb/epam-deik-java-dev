package core.screening;

import core.screening.model.ScreeningDto;
import core.screening.persistence.entity.Screening;
import core.screening.persistence.entity.ScreeningId;
import core.screening.persistence.repository.ScreeningRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;

    @Override
    public void createScreening(ScreeningDto screeningDto) {
        Screening screening = new Screening(new ScreeningId(
                screeningDto.getMovieTitle(),
                screeningDto.getRoomName(),
                screeningDto.getStartOfScreening()
        ));
        screeningRepository.save(screening);
    }

    @Override
    public void deleteScreening(ScreeningDto screeningDto) {
        Optional<Screening> screening = screeningRepository.findByScreeningId(
                new ScreeningId(
                        screeningDto.getMovieTitle(),
                        screeningDto.getRoomName(),
                        screeningDto.getStartOfScreening()
                ));
        if (screening.isPresent()) {
            screeningRepository.delete(screening.get());
        }
    }

    @Override
    public List<ScreeningDto> getScreeningList() {
        return screeningRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private ScreeningDto convertEntityToDto(Screening screening) {
        ScreeningId screeningId = screening.getScreeningId();
        return new ScreeningDto(
                screeningId.getMovieTitle(),
                screeningId.getRoomName(),
                screeningId.getStartOfScreening()
        );
    }

}
