package com.rapidTicketAccess.RapidTicketAccess.Controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.rapidTicketAccess.RapidTicketAccess.Model.*;
import com.rapidTicketAccess.RapidTicketAccess.Request.DistanceRequest;
import com.rapidTicketAccess.RapidTicketAccess.Service.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final OpenStreetMapService openStreetMapService;

    public TicketController(TicketService ticketService, OpenStreetMapService openStreetMapService) {
        this.ticketService = ticketService;
        this.openStreetMapService = openStreetMapService;
    }



    // Create new ticket
    @PostMapping("/create")
    public Ticket createTicket(@RequestBody Ticket ticket){
        return ticketService.createTicket(ticket);
    }

    // Get all Tickets
    @GetMapping("/all")
    public List<Ticket> getAllTickets(){
        return ticketService.getAllTickets();
    }


    // Get ticket by id
    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id){
        return ticketService.getTicketById(id);
    }

    // Delete Ticket by id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long id){

        Ticket ticket = ticketService.getTicketById(id);
        if(ticket == null) return ResponseEntity.ofNullable("No ticket found");

        ticketService.removeTicket(ticket);
        return ResponseEntity.ok("Ticket removed successfully");
    }


    // Calculating the distance manually
    @PostMapping("/calculate-distance")
    public String calculateDistance(@RequestBody DistanceRequest request){
        return ticketService.calculateDistance(request).toString();
    }


    //============== QR Code generation =================

    @PostMapping("/generate")
    public ResponseEntity<String> generateQRCode(@RequestBody TicketRequest ticketRequest) {
        int numberOfPassengers = Integer.parseInt(ticketRequest.getNumberOfPassengers());

        String source = ticketRequest.getSource();
        String destination = ticketRequest.getDestination();

        String distance = ticketService.calculateDistanceUsingOpenStreetMap(source, destination);
        double amount = 0;
        if(!distance.equals("Invalid source or destination")){
            String[] arr = distance.split(" ");
            amount = Double.parseDouble(arr[arr.length - 1]);
        }

        String paymentUrl = "http://localhost:8080/api/tickets/receipt?source=" + source +
                             "&destination=" + destination +
                             "&numberOfPassengers=" + numberOfPassengers +
                             "&amount=" + amount;

        return ResponseEntity.ok(paymentUrl);
    }


    //============== Process Payment ==============

    @GetMapping("/payment")
    public ResponseEntity<String> processPayment(@RequestParam String source, @RequestParam String destination) {
        // Payment logic

        return ResponseEntity.ok("Payment successful! Your receipt will be generated.");
    }

    //============== Generate Receipt ===============

    @GetMapping("/receipt")
    public ResponseEntity<Resource> downloadReceipt(@RequestParam String source, @RequestParam String destination, @RequestParam int numberOfPassengers, @RequestParam Double amount) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Bus Ticket Recipt"));
            document.add(new Paragraph("Source: " + source));
            document.add(new Paragraph("Destination: " + destination));
            document.add(new Paragraph("Date: " + LocalDate.now()));
            document.add(new Paragraph("Number of Passengers: " + numberOfPassengers));
            document.add(new Paragraph("Amount Paid " + numberOfPassengers * amount));

            document.close();

            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Ticket_Receipt.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



    //====================== Calculating the distance using the Open Street Map api =========================

    //Get the Latitude and longitude of the location
    @GetMapping("/location/{name}")
    public String getLatLong(@PathVariable String name){
        return openStreetMapService.getLatLong(name);
    }

    //Calculate the distance by using Open Street Map api
    @PostMapping("/calculate-distance-api")
    public String calculateDistanceUsingOpenStreetMap(@RequestBody DistanceRequest request){
        return ticketService.calculateDistanceUsingOpenStreetMap(request.getSource(), request.getDestination());
    }

}


