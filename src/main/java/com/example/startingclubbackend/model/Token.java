package com.example.startingclubbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "tokens")
public class Token {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name ="id" , nullable = false)
    private Long  id  ;

    @Column(name ="token" , unique = true)
    private String token;

    @Column(name ="token" , nullable = false)
    private String tokeType ;

    @Column(name ="token" , nullable = false)
    private boolean expired ;

    @Column(name ="token" , nullable = false)
    private boolean revoked ;
}
