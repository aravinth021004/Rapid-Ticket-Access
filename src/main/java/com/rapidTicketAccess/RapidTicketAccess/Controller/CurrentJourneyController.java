package com.rapidTicketAccess.RapidTicketAccess.Controller;

import com.rapidTicketAccess.RapidTicketAccess.Model.Journey;
import com.rapidTicketAccess.RapidTicketAccess.Request.PassengerCountUpdateRequest;
import com.rapidTicketAccess.RapidTicketAccess.Response.PassengerCountUpdateResponse;
import com.rapidTicketAccess.RapidTicketAccess.Service.CurrentJourneyService;
import com.rapidTicketAccess.RapidTicketAccess.Service.JourneyService;
import com.rapidTicketAccess.RapidTicketAccess.Service.PassengerCountService;
import com.rapidTicketAccess.RapidTicketAccess.Service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/currentJourney")
public class CurrentJourneyController {

    private final TicketService ticketService;
    private final JourneyService journeyService;
    private final PassengerCountService passengerCountService;
    private final CurrentJourneyService currentJourneyService;

    public CurrentJourneyController(TicketService ticketService,
                                    JourneyService journeyService,
                                    PassengerCountService passengerCountService,
                                    CurrentJourneyService currentJourneyService) {
        this.ticketService = ticketService;
        this.journeyService = journeyService;
        this.passengerCountService = passengerCountService;
        this.currentJourneyService = currentJourneyService;
    }

    // To start the journey
    @PostMapping("/startJourney")
    public ResponseEntity<String> startJourney(@RequestBody Long journeyId){
        Journey journey = journeyService.getJourneyById(journeyId);
        currentJourneyService.setJouneyDetails(journey);
        System.out.println("Journey started");
        return ResponseEntity.ok("Journey started successfully");
    }

    // To stop the journey
    @PostMapping("/stopJourney")
    public ResponseEntity<String> stopJourney(@RequestBody String message){
        currentJourneyService.reset();
        System.out.println(message);
        return ResponseEntity.ok("Journey stopped successfully");
    }

    @PostMapping("/updatePassengerCount")
    public ResponseEntity<PassengerCountUpdateResponse> updatePassengerCount(@RequestBody PassengerCountUpdateRequest request) {
        if(currentJourneyService.isJourneyStarted()){
            currentJourneyService.setDestinationCount(request.getDestination(), request.getNumberOfPassengers());
            int newCount = currentJourneyService.getStopCount(request.getDestination());
            int currentStopCount = currentJourneyService.getCurrentStopCount();
            int totalCount = currentJourneyService.getTotalCount();
            PassengerCountUpdateResponse response = new PassengerCountUpdateResponse(newCount, currentStopCount, totalCount);
            return ResponseEntity.ok(response);
        }
        else return ResponseEntity.status(HttpStatus.CONFLICT).body(null);


    }

    @PostMapping("/stop/{currentStop}")
    public ResponseEntity<Integer> reachCurrentStop(@PathVariable String currentStop){
        System.out.println("Current Stop reached: " + currentStop);
        currentJourneyService.setCurrentStopCountAfterStop(currentStop);
        return ResponseEntity.ok(currentJourneyService.getCurrentStopCount());
    }

//    @PostMapping("/stop")

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
