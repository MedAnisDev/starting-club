package com.example.startingclubbackend.model.event;

import com.example.startingclubbackend.model.user.athlete.Athlete;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "event_performance")
public class EventPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "athlete_id")
    private Athlete athlete ;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event ;

    @Column(name = "note")
    private double noteEvent;


}

