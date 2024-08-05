package com.example.startingclubbackend.model.token;

import com.example.startingclubbackend.model.user.Athlete;
import com.example.startingclubbackend.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "confirm_tokens")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token" ,nullable = false, unique = true)
    private String token;

    @Column(name = "created_at" ,nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expired_at" , nullable = false)
    private LocalDateTime expiredAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private Athlete athlete;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt, Athlete athlete) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.athlete = athlete;
    }
}
