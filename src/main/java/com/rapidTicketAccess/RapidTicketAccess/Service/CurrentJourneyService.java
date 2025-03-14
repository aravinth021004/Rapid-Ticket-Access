package com.rapidTicketAccess.RapidTicketAccess.Service;

import com.rapidTicketAccess.RapidTicketAccess.Model.CurrentJourney;
import org.springframework.stereotype.Service;

@Service
public class CurrentJourneyService {

    private CurrentJourney currentJourney;

    public CurrentJourneyService(){
        currentJourney = new CurrentJourney();
    }

    //Set the current stop count
    public void setCurrentStopCountAfterStop(String currentStop){
        int currentStopCount = currentJourney.getCurrentStopCount();
        currentStopCount -= currentJourney.getDestinationCount(currentStop);
        currentJourney.setCurrentStopCount(currentStopCount);
    }

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
        currentJourney.setTotalCount(0);
        currentJourney.setCurrentStopCount(0);
        currentJourney.resetDestinationCount();
    }
}
