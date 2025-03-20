package com.rapidTicketAccess.RapidTicketAccess.Controller;

import com.rapidTicketAccess.RapidTicketAccess.Response.PaymentResponse;
import com.rapidTicketAccess.RapidTicketAccess.Service.PaymentService;
import com.rapidTicketAccess.RapidTicketAccess.Service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final TicketService ticketService;

    PaymentController(PaymentService paymentService, TicketService ticketService) {
        this.paymentService = paymentService;
        this.ticketService = ticketService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handlePaymentWebHook(@RequestBody PaymentResponse paymentResponse) {
        String transactionId = paymentResponse.getTransactionId();
        boolean isSuccess = paymentResponse.isSuccess();
        String paymentStatus = "pending";
        if(isSuccess) {
            // Update transaction as successful
            paymentService.markTransactionAsPaid(transactionId);
            paymentStatus = "paid";
            String receiptPath = "";
//            String receiptPath = ticketService.generatePdfReceipt(
//                    paymentResponse.getSource(),
//                    paymentResponse.getDestination(),
//                    Integer.parseInt(paymentResponse.getNumberOfPassengers())
//            );

            return ResponseEntity.ok("Payment successful. Receipt generated: " + receiptPath);
        } else {
            // Payment failed
            paymentStatus = "failed";
            paymentService.markTransactionAsFailed(transactionId);
            return ResponseEntity.badRequest().body("Payment failed.");
        }
    }
}
