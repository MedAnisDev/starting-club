package com.example.startingclubbackend.model.announcement;

import com.example.startingclubbackend.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name ="announcements")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true , nullable = false)
    private Long id ;

    @Column(name = "title" , nullable = false)
    private String title ;

    @Column(name = "content" , nullable = false , columnDefinition = "TEXT")
    private String content ;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now() ;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now() ;

    @ManyToOne()
    @JoinColumn(name = "created_by")
    private User createdBy;
}