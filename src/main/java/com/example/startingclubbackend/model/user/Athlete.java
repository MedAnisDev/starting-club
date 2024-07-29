package com.example.startingclubbackend.model.user;

import com.example.startingclubbackend.model.event.Event;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "athletes")
@PrimaryKeyJoinColumn(name = "id")
public class Athlete extends User {

    @Column( name = "licence_id", unique=true )
    private String licenceID ;

    @Column( name = "note", unique=true )
    private String note ;

    @Column(name = "date_of_birth" , nullable=false)
    private LocalDate dateOFBirth ;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "athlete_events" ,
            joinColumns = @JoinColumn(name = "athlete_id") ,
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @JsonBackReference
    private List<Event> registeredEvents ;

    public static AthleteBuilder builder(){
        return new AthleteBuilder();
    }
}
