package com.rapidTicketAccess.RapidTicketAccess.Request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PassengerCountUpdateRequest {
    String destination;
    int numberOfPassengers;
}
