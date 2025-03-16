package com.rapidTicketAccess.RapidTicketAccess.Repository;

import com.rapidTicketAccess.RapidTicketAccess.Model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    PaymentTransaction findByTransactionId(String transactionId);
}
