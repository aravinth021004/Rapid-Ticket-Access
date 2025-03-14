package com.rapidTicketAccess.RapidTicketAccess.Repository;

import com.rapidTicketAccess.RapidTicketAccess.Model.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface JourneyRepository extends JpaRepository<Journey, Long> {

    @Query("select journey.id from Journey journey where journey.startPlace = :startPlace AND journey.endPlace = :endPlace")
    Long getJourneyByStartAndEndPlaces(@Param("startPlace") String startPlace,
                                       @Param("endPlace") String endPlace);

    Journey findJourneyById(Long id);
}
