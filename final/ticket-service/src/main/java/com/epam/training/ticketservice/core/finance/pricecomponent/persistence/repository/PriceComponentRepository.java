package com.epam.training.ticketservice.core.finance.pricecomponent.persistence.repository;

import com.epam.training.ticketservice.core.finance.pricecomponent.persistence.entity.PriceComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceComponentRepository extends JpaRepository<PriceComponent, Integer> {

    Optional<PriceComponent> findByName(String name);

}
