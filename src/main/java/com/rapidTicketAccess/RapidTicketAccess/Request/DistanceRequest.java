package com.rapidTicketAccess.RapidTicketAccess.Request;

import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DistanceRequest {
    private Long journeyId;
    private String source;
    private String destination;
    private int numberOfPassengers;
}
