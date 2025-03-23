package com.rapidTicketAccess.RapidTicketAccess.Controller;

import com.rapidTicketAccess.RapidTicketAccess.Model.Journey;
import com.rapidTicketAccess.RapidTicketAccess.Service.JourneyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/api/journey")
public class JourneyController {

    private final JourneyService journeyService;

    public JourneyController(JourneyService journeyService) {
        this.journeyService = journeyService;
    }

    // Create new Journey
    @PostMapping("/create")
    public Journey createJourney(@RequestBody Journey journey){
        return journeyService.createJourney(journey);
    }

    @GetMapping("/all")
    public List<Journey> getAllJourney(){
        return journeyService.getAllJourneys();
    }

    @GetMapping("/{id}")
    public Journey getJourneyById(@PathVariable Long id){
        return journeyService.getJourneyById(id);
    }

    // Modify journey by id
    @PutMapping("/modify/{id}")
    public ResponseEntity<String> modifyJourney(@PathVariable Long id, @RequestBody Journey journey) {
        journeyService.modifyJourney(id, journey);
        return ResponseEntity.ok("Modified Journey");
    }

    // Delete Ticket by id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long id){
        Journey journey = journeyService.getJourneyById(id);
        if(journey == null) return ResponseEntity.ofNullable("No Journey found");

        journeyService.removeJourney(journey);
        return ResponseEntity.ok("Journey removed successfully!");
    }
}
