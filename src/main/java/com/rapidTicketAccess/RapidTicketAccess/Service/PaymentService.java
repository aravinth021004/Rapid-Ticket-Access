package com.rapidTicketAccess.RapidTicketAccess.Service;

import com.rapidTicketAccess.RapidTicketAccess.Model.PaymentTransaction;
import com.rapidTicketAccess.RapidTicketAccess.Repository.PaymentTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private PaymentTransactionRepository paymentTransactionRepository;


    private final Map<String, PaymentTransaction> transactions = new HashMap<>();

    public PaymentTransaction getTransaction(String transactionId) {
        return paymentTransactionRepository.findByTransactionId(transactionId);
    }

    public void CreateTransaction(String transactionId, String source, String destination, int numberOfPassengers) {
        transactions.put(transactionId, new PaymentTransaction(transactionId, source, destination, numberOfPassengers, "PENDING"));
    }

    public void markTransactionAsPaid(String transactionId) {
        if(transactions.containsKey(transactionId)) {
            transactions.get(transactionId).setStatus("PAID");
        }
    }

    public void markTransactionAsFailed(String transactionId) {
        if (transactions.containsKey(transactionId)) {
            transactions.get(transactionId).setStatus("FAILED");
        }
    }
}
