package com.example.startingclubbackend.model.token;

import com.example.startingclubbackend.model.user.Admin;
import com.example.startingclubbackend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name ="id" , unique=true , nullable = false)
    private Long  id  ;

    @Column(name ="token" , unique = true)
    private String token;

    @Column(name ="expired" , nullable = false)
    private boolean expired ;

    @Column(name ="revoked" , nullable = false)
    private boolean revoked ;

    @Column(name = "issued_at", nullable = false)
    private Date issuedAt;

    @Column(name = "expires_at", nullable = false)
    private  Date expiresAt;

    @ManyToOne
    @JoinColumn(name = "user_id" ,referencedColumnName = "id")
    User user ;

}
