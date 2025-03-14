package com.rapidTicketAccess.RapidTicketAccess.Service;

import com.rapidTicketAccess.RapidTicketAccess.Model.Journey;
import com.rapidTicketAccess.RapidTicketAccess.Model.Stop;
import com.rapidTicketAccess.RapidTicketAccess.Repository.JourneyRepository;
import com.rapidTicketAccess.RapidTicketAccess.Repository.StopRepository;
import com.rapidTicketAccess.RapidTicketAccess.Request.StopRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StopService {

    private final StopRepository stopRepository;
    private final JourneyService journeyService;
    private final StopRequest stopRequest;

    public StopService(StopRepository stopRepository, JourneyService journeyService, StopRequest stopRequest) {
        this.stopRepository = stopRepository;
        this.journeyService = journeyService;
        this.stopRequest = stopRequest;
    }

    //Add the stop for the respective journey
    public void addStopToJourney(Stop stop, Long journeyId){
        Journey journey = journeyService.getJourneyById(journeyId);
        stop.setJourney(journey);
        journey.addStop(stop);
        journeyService.modifyJourney(journeyId, journey);
        stopRepository.save(stop);
    }

    // Get the stop by id
    public Stop getStopById(Long id){
        return stopRepository.getReferenceById(id);
    }

    // Get all the stops
    public List<Stop> getAllStops(){
        return stopRepository.findAll();
    }


    // Get stops by journey
    public List<Stop> getStopsByJourneyId(Long journeyId){
        return stopRepository.findByJourneyId(journeyId);
    }

    // Modify the stop
    public void modifyStop(Long id, Stop newStop){
        Stop oldStop = stopRepository.getReferenceById(id);
        oldStop.setId(newStop.getId());
        oldStop.setCurrentStop(newStop.getCurrentStop());
        oldStop.setDistanceFromStart(newStop.getDistanceFromStart());
        oldStop.setJourney(newStop.getJourney());
    }

    //Remove the stop
    public void removeStopById(Long id){
        stopRepository.deleteById(id);
    }

    // Get the stops by the journey
    public List<Stop> getStopsByJourney(Journey journey){
        return journey.getStops();
    }

//    public Stop addStopToJourney(Long journeyId, StopRequest request) {
//        Journey journey = journeyService.getJourneyById(journeyId);
//        if(journey == null) {
//            throw new RuntimeException("Journey not found with id: " + journeyId);
//        }
////        Stop stop = new Stop(request.getCurrentStop(), request.getDistanceFromStart(), journey);
////        journey.addStop(stop);
////        journeyService.modifyJourney(journeyId, journey);
////        return stop;
//    }
}
