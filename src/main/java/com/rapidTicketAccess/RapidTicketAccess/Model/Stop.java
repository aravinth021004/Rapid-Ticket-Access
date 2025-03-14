package com.rapidTicketAccess.RapidTicketAccess.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Stop")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Stop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String currentStop;

    private int distanceFromStart;

    @ManyToOne
    @JoinColumn(name = "journey_id", nullable = false)
    @JsonIgnore
    private Journey journey;

}
