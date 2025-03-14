package com.rapidTicketAccess.RapidTicketAccess.Service;

import com.rapidTicketAccess.RapidTicketAccess.Model.Journey;
import com.rapidTicketAccess.RapidTicketAccess.Model.PassengerCount;
import com.rapidTicketAccess.RapidTicketAccess.Repository.PassengerCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PassengerCountService {

    private final PassengerCountRepository passengerCountRepostiory;
    private final JourneyService journeyService;

    public PassengerCountService(PassengerCountRepository passengerCountRepostiory, JourneyService journeyService) {
        this.passengerCountRepostiory = passengerCountRepostiory;
        this.journeyService = journeyService;
    }

    //Add new Records
    public void addNewStopCountRecords(PassengerCount records){
        passengerCountRepostiory.save(records);
    }

    // Get stop records by the journey Id and date and time
    public List<PassengerCount> getStopCountRecordsByJourneyId(Long journeyId, LocalDateTime timeStamp){
        Journey journey = journeyService.getJourneyById(journeyId);
        return passengerCountRepostiory.findByJourneyAndTimestamp(journey, timeStamp);
    }


}
