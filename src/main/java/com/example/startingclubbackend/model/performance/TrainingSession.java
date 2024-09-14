package com.example.startingclubbackend.model.performance;

import com.example.startingclubbackend.model.user.admin.Admin;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "training_sessions")
public class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "date")
    private LocalDate date ;

    @Column(name = "session_note" , nullable = false)
    private double sessionNote = 0 ;


    @Column(name = "created_at" , nullable = false)
    private LocalDateTime createdAT  = LocalDateTime.now() ;

    @Column(name = "updated_at" , nullable = false)
    private LocalDateTime updatedAT;

    //relations
    @ManyToOne
    @JoinColumn(name = "created_by" , nullable = false)
    private Admin createdBy ;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private Admin updatedBy ;

    @ManyToOne
    @JoinColumn(name = "performance_id")
    private Performance performance ;


    public TrainingSession() {}
}
