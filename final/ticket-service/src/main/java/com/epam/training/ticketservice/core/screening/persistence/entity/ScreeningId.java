package com.epam.training.ticketservice.core.screening.persistence.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
public class ScreeningId implements Serializable {

    private String movieTitle;

    private String roomName;

    private LocalDateTime startOfScreening;

}
