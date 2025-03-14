package com.rapidTicketAccess.RapidTicketAccess.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "PassengerCount")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PassengerCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "journey_id", nullable = false)
    private Journey journey;

    private String currentStop;


    private int currentStopCount;

    private int totalCount;

    private LocalDateTime timestamp;

    public PassengerCount(Journey journey, String currentStop, int currentStopCount, int totalCount, LocalDateTime timestamp){
        this.journey = journey;
        this.currentStop = currentStop;
        this.currentStopCount = currentStopCount;
        this.totalCount = totalCount;
        this.timestamp = timestamp;
    }
}
