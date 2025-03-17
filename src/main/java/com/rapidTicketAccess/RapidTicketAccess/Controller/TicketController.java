package com.rapidTicketAccess.RapidTicketAccess.Controller;

import com.rapidTicketAccess.RapidTicketAccess.Request.TicketRequest;
import com.rapidTicketAccess.RapidTicketAccess.Service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
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

    @PostMapping("/generate/{process}")
    public ResponseEntity<String> generateTicket(@RequestBody TicketRequest ticketRequest, @PathVariable String process) {
        String source = ticketRequest.getSource();
        String destination = ticketRequest.getDestination();
        int numberOfpassengers = Integer.parseInt(ticketRequest.getNumberOfPassengers());

        String transactionId = LocalDateTime.now().toString();

//        paymentService.createTransaction(transactionId, source, destination, numberOfpassengers);

        String receiptPath = "";
        String paymentUrl = "";

        // Payment link and logic
        if(process.equalsIgnoreCase("ProcessToPayment")) {
            paymentUrl = "Payment through payment gateway integration";
        }

        if(process.equalsIgnoreCase("TestMode")) {
            paymentUrl = "Payment through test mode";
            paymentService.markTransactionAsPaid(transactionId);

            receiptPath = ticketService.generatePdfReceipt(source, destination, numberOfpassengers);



        }

        // Generate QR Code (Using API)
        String qrCodeUrl = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" + receiptPath;

        return ResponseEntity.ok().body(qrCodeUrl);
    }


}


