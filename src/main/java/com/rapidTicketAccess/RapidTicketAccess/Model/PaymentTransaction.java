package com.rapidTicketAccess.RapidTicketAccess.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transactions")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private int passengers;

    @Column(nullable = false)
    private double amountPaid;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public PaymentTransaction(String transactionId, String source, String destination, int passengers, String status) {
        this.transactionId = transactionId;
        this.source = source;
        this.destination = destination;
        this.passengers = passengers;
        this.status = status;
    }

}
