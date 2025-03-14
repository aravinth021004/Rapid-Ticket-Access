package com.rapidTicketAccess.RapidTicketAccess.Model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "journey")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Journey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String startPlace;
    private String endPlace;

    @OneToMany(mappedBy = "journey", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Stop> stops = new ArrayList<>();

    public void addStop(Stop stop) {
        stops.add(stop);
        stop.setJourney(this);
    }
}
