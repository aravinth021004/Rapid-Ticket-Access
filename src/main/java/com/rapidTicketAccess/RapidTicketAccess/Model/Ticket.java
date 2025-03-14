package com.rapidTicketAccess.RapidTicketAccess.Model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String source; // passenger's Source
    private String destination; // Passenger's Destination
    private int fare; // Ticket price
    private String paymentStatus;
    private LocalDateTime timestamp;

    public Ticket(String source, String destination, int fare, String paymentStatus, LocalDateTime timestamp){
        this.source = source;
        this.destination = destination;
        this.fare = fare;
        this.paymentStatus = paymentStatus;
        this.timestamp = timestamp;
    }
}
