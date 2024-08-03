package com.example.startingclubbackend.model.event;

import com.example.startingclubbackend.model.user.Admin;
import com.example.startingclubbackend.model.user.Athlete;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title" , nullable = false)
    private String title ;

    @Column(name = "location" , nullable = false)
    private String location ;

    @Column(name = "description" , columnDefinition = "TEXT")
    private String description ;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now() ;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt ;

    @Column(name = "date" , nullable = false)
    private LocalDateTime date ;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Admin created_by ;

    @ManyToMany(mappedBy = "registeredEvents")
    @JsonManagedReference
    private List<Athlete> participants ;
}
