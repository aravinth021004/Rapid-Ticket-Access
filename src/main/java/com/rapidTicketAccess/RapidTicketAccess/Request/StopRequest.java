package com.rapidTicketAccess.RapidTicketAccess.Request;


import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StopRequest {
    private String currentStop;
    private int distanceFromStart;
}
