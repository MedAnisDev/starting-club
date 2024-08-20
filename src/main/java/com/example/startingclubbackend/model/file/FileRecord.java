package com.example.startingclubbackend.model.file;

import com.example.startingclubbackend.model.announcement.Announcement;
import com.example.startingclubbackend.model.event.Event;
import com.example.startingclubbackend.model.user.Admin;
import com.example.startingclubbackend.model.user.Athlete;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@Data

@Entity
@Table(name = "files")
public class FileRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name" , nullable = false)
    private String name;

    @Column(name = "type" , nullable = false)
    private String type;

    @Column(name = "path" , nullable = false , unique = true)
    private String path;

    @Column(name = "uploaded_at" , nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private Admin uploadedBy ;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event ;

    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @ManyToOne
    @JoinColumn(name = "athlete_id")
    private Athlete athlete;
    public FileRecord() {

    }
}
