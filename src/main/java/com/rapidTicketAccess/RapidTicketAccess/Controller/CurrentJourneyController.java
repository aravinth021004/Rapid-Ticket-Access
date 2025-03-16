package com.rapidTicketAccess.RapidTicketAccess.Controller;

import com.rapidTicketAccess.RapidTicketAccess.Request.DistanceRequest;
import com.rapidTicketAccess.RapidTicketAccess.Model.Journey;
import com.rapidTicketAccess.RapidTicketAccess.Model.PassengerCount;
import com.rapidTicketAccess.RapidTicketAccess.Model.Ticket;
import com.rapidTicketAccess.RapidTicketAccess.Service.CurrentJourneyService;
import com.rapidTicketAccess.RapidTicketAccess.Service.JourneyService;
import com.rapidTicketAccess.RapidTicketAccess.Service.PassengerCountService;
import com.rapidTicketAccess.RapidTicketAccess.Service.TicketService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/currentJourney")
public class CurrentJourneyController {
    private boolean isJourneyStarted = false;

    private CurrentJourneyService currentJourneyService;

    private final TicketService ticketService;
    private final JourneyService journeyService;
    private final PassengerCountService passengerCountService;

    public CurrentJourneyController(TicketService ticketService,
                                    JourneyService journeyService,
                                    PassengerCountService passengerCountService){
        this.ticketService = ticketService;
        this.journeyService = journeyService;
        this.passengerCountService = passengerCountService;
    }

    // To start the journey
    @PostMapping("/startJourney")
    public void startJourney(){
        isJourneyStarted = true;
        currentJourneyService = new CurrentJourneyService();
        System.out.println("Journey started");
    }

    // To stop the journey
    @PostMapping("/stopJourney")
    public void stopJourney(){
        isJourneyStarted = false;
        currentJourneyService.reset();
        currentJourneyService = null;
        System.out.println("Journey stopped");
    }

    @PostMapping("/stop/{currentStop}")
    public void reachCurrentStop(@PathVariable String currentStop){
        currentJourneyService.setCurrentStopCountAfterStop(currentStop);
    }

    // Generate the tickets3
//    @GetMapping("/generate-ticket")
//    public Ticket generateTicket(@RequestBody DistanceRequest request){
//        if(!isJourneyStarted) return null;
//        Long journeyId = request.getJourneyId();
//        Journey journey = journeyService.getJourneyById(journeyId);
//        String source = request.getSource();
//        String destination = request.getDestination();
//
//        //Creating the ticket
//        Ticket ticket = ticketService.generateTicket(request);
//
//        LocalDateTime timestamp = LocalDateTime.now();
//
//        currentJourneyService.setDestinationCount(destination, request.getNumberOfPassengers());
//        currentJourneyService.setCurrentStopCount(request.getNumberOfPassengers());
//        currentJourneyService.setTotalStopCount(request.getNumberOfPassengers());
//
//        //Adding the record
//        PassengerCount record = new PassengerCount(journey, source,
//                                                   currentJourneyService.getCurrentStopCount(),
//                                                   currentJourneyService.getTotalCount(),
//                                                   timestamp);
//
//        passengerCountService.addNewStopCountRecords(record);
//
//
//        return ticket;
//    }

}
