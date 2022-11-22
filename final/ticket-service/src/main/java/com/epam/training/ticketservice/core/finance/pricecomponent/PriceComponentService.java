package com.epam.training.ticketservice.core.finance.pricecomponent;

import com.epam.training.ticketservice.core.finance.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;

import java.util.List;
import java.util.Optional;

public interface PriceComponentService {

    void createPriceComponent(PriceComponentDto priceComponentDto);

    Optional<PriceComponentDto> getPriceComponentByName(String priceComponentName);

    Optional<PriceComponentDto> getBasePriceComponent();

    List<PriceComponentDto> getPriceComponentsByMovie(String movieTitle);

    List<PriceComponentDto> getPriceComponentsByRoom(String roomName);

    List<PriceComponentDto> getPriceComponentsByScreening(ScreeningDto screeningDto);

    void updateBasePriceComponent(Integer newBasePrice);

    void addPriceComponentToRoom(PriceComponentDto priceComponentDto, String roomName);

    void addPriceComponentToMovie(PriceComponentDto priceComponentDto, String movieTitle);

    void addPriceComponentToScreening(PriceComponentDto priceComponentDto, ScreeningDto screeningDto);
}
