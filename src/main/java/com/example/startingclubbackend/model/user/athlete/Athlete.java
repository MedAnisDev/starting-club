package com.example.startingclubbackend.model.user.athlete;

import com.example.startingclubbackend.model.event.Event;

import com.example.startingclubbackend.model.file.FileRecord;
import com.example.startingclubbackend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "athletes")
@PrimaryKeyJoinColumn(name = "id")
public class Athlete extends User {

    @Column(name = "licence_id", unique = true)
    private String licenceID;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "age")
    private Integer age;

    @Column(name = "has_medal")
    private Boolean hasMedal;

    @Enumerated(EnumType.STRING)
    private AthleteBranch branch ;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "athlete_events",
            joinColumns = @JoinColumn(name = "athlete_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> registeredEvents;

    @OneToMany(mappedBy = "athlete" , cascade = CascadeType.ALL, orphanRemoval = true)
    List<FileRecord> files = new ArrayList<>();

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
