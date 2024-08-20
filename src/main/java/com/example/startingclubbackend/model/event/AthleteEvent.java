package com.example.startingclubbackend.model.event;

import com.example.startingclubbackend.model.user.Athlete;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "athlete_event")
public class AthleteEvent {

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
    private String noteEvent ="NA";


}

