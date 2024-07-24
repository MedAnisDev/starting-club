package com.example.startingclubbackend.model.user;

import com.example.startingclubbackend.model.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

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

    public static AthleteBuilder builder(){
        return new AthleteBuilder();
    }

}
