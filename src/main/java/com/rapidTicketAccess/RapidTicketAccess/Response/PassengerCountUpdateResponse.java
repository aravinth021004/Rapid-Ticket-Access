package com.rapidTicketAccess.RapidTicketAccess.Response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PassengerCountUpdateResponse {
    int newCount;
    int currentStopCount;
    int totalCount;
}
