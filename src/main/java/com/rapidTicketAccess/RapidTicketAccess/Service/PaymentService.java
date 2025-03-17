package com.rapidTicketAccess.RapidTicketAccess.Service;

import com.rapidTicketAccess.RapidTicketAccess.Model.PaymentTransaction;
import com.rapidTicketAccess.RapidTicketAccess.Repository.PaymentTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final TicketService ticketService;

    public PaymentService(PaymentTransactionRepository paymentTransactionRepository, TicketService ticketService) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.ticketService = ticketService;
    }

    private final Map<String, PaymentTransaction> transactions = new HashMap<>();

    public PaymentTransaction getTransaction(String transactionId) {
        return paymentTransactionRepository.findByTransactionId(transactionId);
    }

    public void createTransaction(String transactionId, String source, String destination, int numberOfPassengers) {

        String str = ticketService.calculateDistanceUsingOpenStreetMap(source, destination);
        double distance = 0;
        if(!str.equalsIgnoreCase("Invalid source or destination")){
            String[] arr = str.split(" ");
            distance = Double.parseDouble(arr[arr.length - 1]);
        }
        double amount = distance * numberOfPassengers;

        transactions.put(transactionId, new PaymentTransaction(transactionId, source, destination, numberOfPassengers, amount, "PENDING"));
        paymentTransactionRepository.save(transactions.get(transactionId));
    }

    public void markTransactionAsPaid(String transactionId) {
        if(transactions.containsKey(transactionId)) {
            transactions.get(transactionId).setStatus("PAID");
            paymentTransactionRepository.save(transactions.get(transactionId));
        }
    }

    public void markTransactionAsFailed(String transactionId) {
        if (transactions.containsKey(transactionId)) {
            transactions.get(transactionId).setStatus("FAILED");
            paymentTransactionRepository.save(transactions.get(transactionId));
        }
    }
}
