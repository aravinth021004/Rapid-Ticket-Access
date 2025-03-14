package com.rapidTicketAccess.RapidTicketAccess.Repository;

import com.rapidTicketAccess.RapidTicketAccess.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
