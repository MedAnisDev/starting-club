package com.example.startingclubbackend.model.user;

import com.example.startingclubbackend.model.event.Event;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "athletes")
@PrimaryKeyJoinColumn(name = "id")
public class Athlete extends User {

    @Column(name = "licence_id", unique = true)
    private String licenceID;

    @Column(name = "note", unique = true)
    private String note;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "age")
    private Integer age;

    @Column(name = "has_medal")
    private Boolean hasMedal;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "athlete_events",
            joinColumns = @JoinColumn(name = "athlete_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> registeredEvents;

    //constructor
    public Athlete() {}
    public static AthleteBuilder builder() {
        return new AthleteBuilder();
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    public void setExactAge(){
        if(dateOfBirth != null){
            this.age = Period.between(this.dateOfBirth , LocalDate.now()).getYears() ;
        }
    }
}
