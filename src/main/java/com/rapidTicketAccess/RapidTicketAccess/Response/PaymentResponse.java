package com.rapidTicketAccess.RapidTicketAccess.Response;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String transactionId;
    private boolean success;
    private String source;
    private String destination;
    private String NumberOfPassengers;
}
