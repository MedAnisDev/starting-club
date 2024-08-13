package com.example.startingclubbackend.model.user;

import com.example.startingclubbackend.model.role.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class AthleteBuilder {
    private Long id ;
    private String firstname ;

    private String lastname;

    private String email;
    private String password;
    private LocalDateTime createdAT ;

    private String phoneNumber ;
    private Role role ;

    private String licenceID ;
    private String note ;
    private LocalDate dateOfBirth;
    private Integer age ;
    private Boolean hasMedal ;
    private boolean enable = false; // Default to false


    public AthleteBuilder id(Long id) {
        this.id = id ;
        return this;
    }
    public AthleteBuilder firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }
    public AthleteBuilder lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }
    public AthleteBuilder email(String email) {
        this.email = email;
        return this;
    }
    public AthleteBuilder password(String password) {
        this.password = password;
        return this;
    }
    public AthleteBuilder createAt(LocalDateTime createdAT) {
        this.createdAT = createdAT;
        return this;
    }
    public AthleteBuilder phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
    public AthleteBuilder role(Role role) {
        this.role = role;
        return this;
    }

    public AthleteBuilder licenceID(String licenceID) {
        this.licenceID = licenceID;
        return this;
    }
    public AthleteBuilder note(String note) {
        this.note = note;
        return this;
    }
    public AthleteBuilder dateOFBirth(LocalDate dateOFBirth) {
        this.dateOfBirth = dateOFBirth;
        return this;
    }
    public AthleteBuilder hasMedal(Boolean hasMedal) {
        this.hasMedal = hasMedal;
        return this;
    }

    public AthleteBuilder enable(boolean enable) {
        this.enable = enable;
        return this;
    }

    public Athlete build(){
        Athlete athlete = new Athlete() ;
        athlete.setId(id);
        athlete.setFirstname(firstname);
        athlete.setLastname(lastname);
        athlete.setEmail(email);
        athlete.setPassword(password);
        athlete.setCreatedAT(createdAT);
        athlete.setPhoneNumber(phoneNumber);
        athlete.setRole(role);
        athlete.setLicenceID(licenceID);
        athlete.setNote(note);
        athlete.setDateOFBirth(dateOfBirth);
        athlete.setHasMedal(hasMedal);
        athlete.setAge(Period.between(dateOfBirth , LocalDate.now()).getYears());
        athlete.setEnable(enable);
        return athlete ;
    }


}
