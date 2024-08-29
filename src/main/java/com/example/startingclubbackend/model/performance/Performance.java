package com.example.startingclubbackend.model.performance;

import com.example.startingclubbackend.model.user.admin.Admin;
import com.example.startingclubbackend.model.user.athlete.Athlete;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "performances")
public class Performance {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id ;

    @Column(name = "federation_note")
    private double federationNote = 0 ;

    @Column(name = "created_at" , nullable = false)
    private LocalDateTime createdAT  = LocalDateTime.now() ;

    @Column(name = "updated_at" , nullable = false)
    private LocalDateTime updatedAT;

    //relations
    @ManyToOne
    @JoinColumn(name = "created_by" , nullable = false)
    private Admin createdBy ;

    @ManyToOne
    @JoinColumn(name = "updated_by" )
    private Admin updatedBy ;

    @OneToOne
    @JoinColumn(name = "athlete_id" )
    private Athlete athlete ;

    @OneToMany(mappedBy = "performance" , cascade = CascadeType.ALL)
    List<TrainingSession> trainingSessionList ;
}
