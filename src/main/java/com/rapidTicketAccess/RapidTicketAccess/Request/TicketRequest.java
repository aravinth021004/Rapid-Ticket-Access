package com.rapidTicketAccess.RapidTicketAccess.Request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {
    private String source;
    private String destination;
    private String numberOfPassengers;
}
