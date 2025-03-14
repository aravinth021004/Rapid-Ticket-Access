package com.rapidTicketAccess.RapidTicketAccess.Controller;

import com.rapidTicketAccess.RapidTicketAccess.Model.Journey;
import com.rapidTicketAccess.RapidTicketAccess.Model.Stop;
import com.rapidTicketAccess.RapidTicketAccess.Request.StopRequest;
import com.rapidTicketAccess.RapidTicketAccess.Service.JourneyService;
import com.rapidTicketAccess.RapidTicketAccess.Service.StopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stops")
public class StopController {

    private final StopService stopService;
    private final JourneyService journeyService;

    public StopController(StopService stopService, JourneyService journeyService) {
        this.stopService = stopService;
        this.journeyService = journeyService;
    }

    // Add a stop to the journey
    @PostMapping("/addStop/{journeyId}")
    public ResponseEntity<?> addStops(@RequestBody Stop stop, @PathVariable Long journeyId){
        System.out.println(journeyId);
        stopService.addStopToJourney(stop, journeyId);
        return ResponseEntity.ok(stop);
    }


    //Add a stop to the journey
//    @PostMapping("/addStop/{journeyId}")
//    public ResponseEntity<?> addStops(@RequestBody StopRequest request, @PathVariable Long journeyId){
//        System.out.println(journeyId);
//        Stop addedStop = stopService.addStopToJourney(journeyId, request);
//        return ResponseEntity.ok(addedStop);
//    }

//    // Get the stop by the stop id
//    @GetMapping("/getStop/{stopId}")
//    public Stop getStop(@PathVariable Long stopId){
//        return stopService.getStopById(stopId);
//    }

    // Get the stop list by Journey id
    @GetMapping("/getStops/{journeyId}")
    public List<Stop> getStopsByJourney(@PathVariable Long journeyId){
        return stopService.getStopsByJourneyId(journeyId);
    }

    // Modify the stop
    @PutMapping("/modifyStop/{currStopId}")
    public ResponseEntity<String> modifyStop(@PathVariable Long currStopId, @RequestBody Stop newStop){
        Stop currStop = stopService.getStopById(currStopId);
        if(currStop == null) return ResponseEntity.ofNullable("Invalid Stop");

        stopService.modifyStop(currStopId, newStop);
        return ResponseEntity.ok("Stop was modified successfully");
    }

    // Delete the stop
    @DeleteMapping("/deleteStop/{stopId}")
    public ResponseEntity<String> deleteStopById(@PathVariable Long stopId){
        Stop stop = stopService.getStopById(stopId);

        if(stop == null) return ResponseEntity.ofNullable("Invalid Stop");

        stopService.removeStopById(stopId);
        return ResponseEntity.ok("Stop removed Successfully");
    }


}
