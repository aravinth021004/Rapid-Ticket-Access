package com.rapidTicketAccess.RapidTicketAccess.Repository;

import com.rapidTicketAccess.RapidTicketAccess.Model.Journey;
import com.rapidTicketAccess.RapidTicketAccess.Model.PassengerCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PassengerCountRepository extends JpaRepository<PassengerCount, Long> {
    List<PassengerCount> findByJourneyAndTimestamp(Journey journey, LocalDateTime timestamp);
}
