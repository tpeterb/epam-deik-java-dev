package com.epam.training.ticketservice.core.finance.pricecomponent.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PriceComponentDto {

    private String name;

    private Integer amount;

}
