package com.epam.training.ticketservice.core.finance.pricecomponent;

import com.epam.training.ticketservice.core.finance.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.core.finance.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.finance.pricecomponent.persistence.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PriceComponentServiceImpl implements PriceComponentService {

    private final PriceComponentRepository priceComponentRepository;

    private final RoomRepository roomRepository;

    private final MovieRepository movieRepository;

    private final ScreeningRepository screeningRepository;

    @Override
    public void createPriceComponent(PriceComponentDto priceComponentDto) {
        PriceComponent priceComponent = new PriceComponent(
                priceComponentDto.getName(),
                priceComponentDto.getAmount()
        );
        priceComponentRepository.save(priceComponent);
    }

    @Override
    public Optional<PriceComponentDto> getPriceComponentByName(String priceComponentName) {
        Optional<PriceComponent> priceComponent = priceComponentRepository.findByName(priceComponentName);
        if (priceComponent.isPresent()) {
            return Optional.of(convertFromEntityToDto(priceComponent.get()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<PriceComponentDto> getBasePriceComponent() {
        Optional<PriceComponent> basePriceComponent = priceComponentRepository.findByName("BasePriceComponent");
        if (basePriceComponent.isPresent()) {
            return Optional.of(convertFromEntityToDto(basePriceComponent.get()));
        }
        return Optional.empty();
    }

    @Override
    public List<PriceComponentDto> getPriceComponentsByMovie(String movieTitle) {
        List<PriceComponentDto> priceComponentDtosToReturn = new ArrayList<PriceComponentDto>();
        List<PriceComponent> priceComponents = priceComponentRepository.findAll();
        for (var priceComponent : priceComponents) {
            for (var movie : priceComponent.getMovies()) {
                if (movie.getTitle().equals(movieTitle)) {
                    priceComponentDtosToReturn.add(new PriceComponentDto(
                            priceComponent.getName(),
                            priceComponent.getAmount()
                    ));
                    break;
                }
            }
        }
        return priceComponentDtosToReturn;
    }

    @Override
    public List<PriceComponentDto> getPriceComponentsByRoom(String roomName) {
        List<PriceComponentDto> priceComponentDtosToReturn = new ArrayList<PriceComponentDto>();
        List<PriceComponent> priceComponents = priceComponentRepository.findAll();
        for (var priceComponent : priceComponents) {
            for (var room : priceComponent.getRooms()) {
                if (room.getName().equals(roomName)) {
                    priceComponentDtosToReturn.add(new PriceComponentDto(
                            priceComponent.getName(),
                            priceComponent.getAmount()
                    ));
                }
            }
        }
        return priceComponentDtosToReturn;
    }

    @Override
    public List<PriceComponentDto> getPriceComponentsByScreening(ScreeningDto screeningDto) {
        List<PriceComponentDto> priceComponentDtosToReturn = new ArrayList<PriceComponentDto>();
        List<PriceComponent> priceComponents = priceComponentRepository.findAll();
        for (var priceComponent : priceComponents) {
            for (var screening : priceComponent.getScreenings()) {
                ScreeningDto tempScreeningDto = new ScreeningDto(
                        screening.getMovieTitle(),
                        screening.getRoomName(),
                        screening.getStartOfScreening()
                );
                if (tempScreeningDto.equals(screeningDto)) {
                    priceComponentDtosToReturn.add(new PriceComponentDto(
                            priceComponent.getName(),
                            priceComponent.getAmount()
                    ));
                }
            }
        }
        return priceComponentDtosToReturn;
    }

    @Override
    public void updateBasePriceComponent(Integer newBasePrice) {
        Optional<PriceComponent> basePriceComponent = priceComponentRepository.findByName("BasePriceComponent");
        if (basePriceComponent.isPresent()) {
            Optional<PriceComponent> basePrice = priceComponentRepository.findById(basePriceComponent.get().getId());
            if (newBasePrice >= 0) {
                basePrice.get().setAmount(newBasePrice);
                priceComponentRepository.save(basePrice.get());
            }
        }
    }

    @Override
    @Transactional
    public void addPriceComponentToRoom(PriceComponentDto priceComponentDto, String roomName) {
        Optional<PriceComponent> priceComponent = priceComponentRepository.findByName(priceComponentDto.getName());
        Optional<Room> room = roomRepository.findByName(roomName);
        if (priceComponent.isPresent() && room.isPresent()) {
            priceComponent.get().getRooms().add(room.get());
        }
    }

    @Override
    @Transactional
    public void addPriceComponentToMovie(PriceComponentDto priceComponentDto, String movieTitle) {
        Optional<PriceComponent> priceComponent = priceComponentRepository.findByName(priceComponentDto.getName());
        Optional<Movie> movie = movieRepository.findByTitle(movieTitle);
        if (priceComponent.isPresent() && movie.isPresent()) {
            priceComponent.get().getMovies().add(movie.get());
        }
    }

    @Override
    @Transactional
    public void addPriceComponentToScreening(PriceComponentDto priceComponentDto, ScreeningDto screeningDto) {
        Optional<PriceComponent> priceComponent = priceComponentRepository.findByName(priceComponentDto.getName());
        Optional<Screening> screening = screeningRepository.findByMovieTitleAndRoomNameAndStartOfScreening(
                screeningDto.getMovieTitle(),
                screeningDto.getRoomName(),
                screeningDto.getStartOfScreening()
        );
        if (priceComponent.isPresent() && screening.isPresent()) {
            priceComponent.get().getScreenings().add(screening.get());
        }
    }

    private PriceComponentDto convertFromEntityToDto(PriceComponent priceComponent) {
        return new PriceComponentDto(
                priceComponent.getName(),
                priceComponent.getAmount()
        );
    }

}
