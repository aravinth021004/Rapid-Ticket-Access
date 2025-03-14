package com.rapidTicketAccess.RapidTicketAccess.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class paymentController {

    @GetMapping("/upi")
    public ResponseEntity<String> generateUPILink(
            @RequestParam String upiId,
            @RequestParam String name,
            @RequestParam String amount,
            @RequestParam String orderId
    ){
        String upiUrl = String.format(
                "upi://pay?pa=%s&pn=%s&mc=&tid=%s&tr=%s&tn=Ticket Payment&am=%s&cu=INR",
                upiId, name, UUID.randomUUID().toString(), orderId, amount
        );
        return ResponseEntity.ok(upiUrl);
    }
}
