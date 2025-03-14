package com.rapidTicketAccess.RapidTicketAccess.Service;

import com.rapidTicketAccess.RapidTicketAccess.Model.Journey;
import com.rapidTicketAccess.RapidTicketAccess.Model.Stop;
import com.rapidTicketAccess.RapidTicketAccess.Repository.JourneyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JourneyService {

    private final JourneyRepository journeyRepository;

    public JourneyService(JourneyRepository journeyRepository) {
        this.journeyRepository = journeyRepository;
    }

    // Add the journey
    public Journey createJourney(Journey journey) {
        return journeyRepository.save(journey);
    }

    // Retrieve the journey
    public Journey getJourneyById(Long id){
        return journeyRepository.findJourneyById(id);
    }

    // Get all the journeys
    public List<Journey> getAllJourneys(){
        return journeyRepository.findAll();
    }

    // Modify the current journey
    public Journey modifyJourney(Long id, Journey newJourney){
        Journey oldJourney = journeyRepository.getReferenceById(id);
        oldJourney.setId(newJourney.getId());
        oldJourney.setStartPlace(newJourney.getStartPlace());
        oldJourney.setEndPlace(newJourney.getEndPlace());
        oldJourney.setStops(newJourney.getStops());

        return oldJourney;
    }

    // Remove the journey
    public void removeJourneyById(Long id){
        Journey journey = journeyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Journey not found with id: " + id));
        journeyRepository.delete(journey);
    }

    public void removeJourney(Journey journey) {
        journeyRepository.delete(journey);
    }
}
