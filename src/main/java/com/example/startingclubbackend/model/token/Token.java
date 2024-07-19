package com.example.startingclubbackend.model.token;

import com.example.startingclubbackend.model.user.User;
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
    @Column(name ="id" , unique=true , nullable = false)
    private Long  id  ;

    @Column(name ="token" , unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    @Column(name ="expired" , nullable = false)
    private boolean expired ;

    @Column(name ="revoked" , nullable = false)
    private boolean revoked ;

    @ManyToOne
    @JoinColumn(name = "user_id" ,referencedColumnName = "id")
    User user ;
}
