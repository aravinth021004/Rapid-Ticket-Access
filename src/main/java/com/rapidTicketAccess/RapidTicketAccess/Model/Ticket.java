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
    private Long id;
    private String source; // passenger's Source
    private String destination; // Passenger's Destination
    private int numberOfPassengers; // Number of Passengers
    private double fare; // Ticket price
    private LocalDateTime timestamp;

    public Ticket(String source, String destination, int NumberOfPassengers, double fare, LocalDateTime timestamp){
        this.source = source;
        this.destination = destination;
        this.numberOfPassengers = NumberOfPassengers;
        this.fare = fare;
        this.timestamp = timestamp;
    }
}
