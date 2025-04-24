package com.rapidTicketAccess.RapidTicketAccess.Service;

import com.rapidTicketAccess.RapidTicketAccess.Model.CurrentJourney;
import com.rapidTicketAccess.RapidTicketAccess.Model.Journey;
import com.rapidTicketAccess.RapidTicketAccess.Model.Stop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrentJourneyService {
    private boolean isJourneyStarted = false;
    private CurrentJourney currentJourney = new CurrentJourney();
    private JourneyService journeyService;

    public CurrentJourneyService(JourneyService journeyService) {
        this.journeyService = journeyService;
    }


    public boolean isJourneyStarted() {
        return isJourneyStarted;
    }

    //Set the current stop count
    public void setCurrentStopCountAfterStop(String currentStop){
        int currentStopCount = currentJourney.getCurrentStopCount();
        currentStopCount -= currentJourney.getDestinationCount(currentStop);
        currentJourney.setCurrentStopCount(currentStopCount);
    }

    public void setSourceAndDestination(String source, String destination){
        currentJourney.setStartPlace(source);
        currentJourney.setEndPlace(destination);
    }

    // Set the current stop count
    public void setCurrentStopCount(int numberOfPassengers){
        currentJourney.setCurrentStopCount(currentJourney.getCurrentStopCount() + numberOfPassengers);
    }

    // Set the total count
    public void setTotalStopCount(int numberOfPassengers){
        currentJourney.setTotalCount(currentJourney.getTotalCount() + numberOfPassengers);
    }


    // Get the current stop count
    public int getCurrentStopCount(){
        return currentJourney.getCurrentStopCount();
    }

    // Get the total count
    public int getTotalCount(){
        return currentJourney.getTotalCount();
    }

    // Add the number of passengers to the destination
    public void setDestinationCount(String destination, int numberOfPassengers) {
        currentJourney.setDestinationCount(destination, numberOfPassengers);
    }


    // Reset all the details
    public void reset(){
        isJourneyStarted = false;
        currentJourney = new CurrentJourney();
    }

    public void setJouneyDetails(Journey journey) {
        isJourneyStarted = true;
        currentJourney.setStartPlace(journey.getStartPlace());
        currentJourney.setEndPlace(journey.getEndPlace());

        List<Stop> stops = journey.getStops();

        for(Stop stop : stops){
            currentJourney.setDestinationCount(stop.getCurrentStop(), 0);
        }

        currentJourney.setCurrentStopCount(0);
        currentJourney.setTotalCount(0);
    }

    public int getStopCount(String destination) {
        return currentJourney.getDestinationCount(destination);
    }
}
