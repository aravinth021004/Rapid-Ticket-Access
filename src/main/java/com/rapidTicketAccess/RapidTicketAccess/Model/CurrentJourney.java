package com.rapidTicketAccess.RapidTicketAccess.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CurrentJourney {
    private Long id;
    private String startPlace;
    private String endPlace;

    private Map<String, Integer> stopWiseCount = new HashMap<>(); // Every Destination count;

    private int currentStopCount;
    private int totalCount; // Total count of the passengers

    public void setDestinationCount(String destination, int numberOfPassengers) {
        stopWiseCount.put(destination, stopWiseCount.getOrDefault(destination, 0) + numberOfPassengers);
    }

    public int getDestinationCount(String destination) {
        return stopWiseCount.getOrDefault(destination, 0);
    }

    public void resetDestinationCount(){
        stopWiseCount.clear();
    }

}
