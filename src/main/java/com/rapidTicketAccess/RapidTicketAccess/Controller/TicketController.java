package com.rapidTicketAccess.RapidTicketAccess.Controller;

import com.rapidTicketAccess.RapidTicketAccess.Request.TicketRequest;
import com.rapidTicketAccess.RapidTicketAccess.Service.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final OpenStreetMapService openStreetMapService;
    private final PaymentService paymentService;

    public TicketController(TicketService ticketService, OpenStreetMapService openStreetMapService, PaymentService paymentService) {
        this.ticketService = ticketService;
        this.openStreetMapService = openStreetMapService;
        this.paymentService = paymentService;
    }



    //============== Ticket generation =================

    @PostMapping("/generate")
    public String generateTicket(@RequestBody TicketRequest ticketRequest) {
        String source = ticketRequest.getSource();
        String destination = ticketRequest.getDestination();
        int numberOfpassengers = Integer.parseInt(ticketRequest.getNumberOfPassengers());

        String transactionId = UUID.randomUUID().toString();

        paymentService.createTransaction(transactionId, source, destination, numberOfpassengers);

        // Payment link
        String paymentUrl = "";

        // Generate QR Code (Using API)
        String qrCodeUrl = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" + paymentUrl;

        return qrCodeUrl;
    }


}


